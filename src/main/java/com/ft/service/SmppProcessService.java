package com.ft.service;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.BaseSm;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.ft.config.ApplicationProperties;
import com.ft.domain.Product;
import com.ft.domain.Provisioning;
import com.ft.domain.Sms;
import com.ft.domain.SmsContent;
import com.ft.domain.SmsLog;
import com.ft.domain.Subscriber;
import com.ft.repository.ProvisioningRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SmsRepository;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;
import com.ft.web.api.model.ChargeRequest;
import com.ft.web.api.model.ChargeResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Scope("singleton")
public class SmppProcessService {

    private static final Logger log = LoggerFactory.getLogger(SmppProcessService.class);

    @Autowired
    private ApplicationProperties props;

    @Autowired
    private SmsLogRepository smsMsgRepo;

    @Autowired
    private SmsRepository smsRepo;

    @Autowired
    private SubscriberRepository msisdnRepo;

    @Autowired
    private SubMsisdnService msisdnSvc;

    @Autowired
    private SubProductRepository productRepo;

    @Autowired
    private SubscriptionService subSvc;

    @Autowired
    private ProvisioningRepository provisioningRepo;
    @Autowired
    private RestTemplate restEndpoint;
    @Autowired
    private SmsContentService msgSvc;

    /**
     * Handling delivery report
     *
     * @param dlr
     */
    public void processDLR(DeliveryReceipt dlr) {
        SmsLog sms = smsMsgRepo.findOneByMessageId(dlr.getMessageId());
        if (sms != null) {
            if (dlr.getState() == SmppConstants.STATE_DELIVERED) {
                sms.setDeliveredAt(ZonedDateTime.now());
                sms.setStatus(SmsLog.STATE_DELIVERED);
                sms.setTag(dlr.getText());
            } else {
                sms.setDeliveredAt(ZonedDateTime.now());
                sms.setStatus(SmsLog.STATE_FAILED);
                sms.setTag(dlr.getText());
            }
            smsMsgRepo.save(sms);
        }
    }

    /**
     * Convert MO SMS to a web trigger
     *
     * @param message
     */
    public void processMO(BaseSm message) {
        Product productTpl = props.getProductTpl();
        String msisdn = message.getSourceAddress().getAddress();
        String shortcode = message.getDestAddress().getAddress();

        String shortMsg = "";
        try {
            Tlv messagePayload = message.getOptionalParameter(SmppConstants.TAG_MESSAGE_PAYLOAD);
            if (messagePayload != null) {
                shortMsg = messagePayload.getValueAsString();
            } else {
                shortMsg = CharsetUtil.decode(message.getShortMessage(), CharsetUtil.CHARSET_GSM);
            }
            log.debug("Got DeliverSM: " + shortMsg);
        } catch (Exception e) {
            log.error("Cannot parse message", e);
        }
        shortMsg = shortMsg.toUpperCase(Locale.ROOT).trim();
        log.info("DeliverSM: " + msisdn + " --> " + shortcode + " : [" + shortMsg + "]");
        Sms smsReq = new Sms().destination(shortcode).deliveredAt(ZonedDateTime.now()).status(SmsLog.STATE_DELIVERED)
                .source(msisdn).text(shortMsg).tag("MO");
        // Response SMS here....
        Sms response = new Sms(props.getSms())
                .destination(msisdn)
                .status(SmsLog.STATE_PENDING)
                .submitAt(ZonedDateTime.now());
        if (shortMsg.contains("HELP")) {
            log.debug("Customer need help... using default msg-not-used template");
            response.text(productTpl.getMsgNotUsed());
        } else if (shortMsg.equalsIgnoreCase("yes") && props.isDoubleOptIn()) {
            // Check confirmation first
            log.debug("Double Opt-in Confirmation");
            // Double confirmation process
            Subscriber pendingSub = msisdnRepo.findOneByMsisdnAndStatus(msisdn, Subscriber.STATUS_NEED_CONFIRM);
            log.debug("Found pending to confirm " + pendingSub);
            if (pendingSub == null) {
                // Sorry, no luck in finding you
                log.debug("Sorry we cannot find your requests");
                response.text(productTpl.getMsgNotUsed());
            } else {
                Provisioning provisioning = new Provisioning();
                Optional<Product> pendingProductOpt = productRepo.findById(pendingSub.getProductId());
                if (pendingProductOpt.isPresent()) {
                	Product pendingProduct = pendingProductOpt.get();
                	List<SmsContent> contents = msgSvc.getTodayContents(pendingProduct);
                	LocalDate ld = LocalDate.now();
                	Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                	Date date = Date.from(instant);
                	if (props.isNotifyChargeFailed()) {
                		provisioning.setNotifyOnFailedBilling(true);
                	}
                	if (props.isPreChargeNotify()) {
                		provisioning.setNotifyBeforeBilling(true);
                	}
                	provisioning.setBillPerSms(true);
                	provisioning.setProductId(pendingProduct.getId());
                	provisioning.setContentId(contents.get(0).getId());
                	provisioning.setMsisdn(pendingSub.getMsisdn());
                	provisioning.setStatus("fresh");
                	provisioning.setTrialCount(0);
                	provisioning.setDateLogged(date);
                	provisioning.setNextRetrial(new Date());
                	if (pendingSub.getTrialCount() == null
                			|| pendingSub.getTrialCount() == 0) {
                		provisioning.setSuccessAverage(1.0);
                	} else {
                		int trialCount = pendingSub.getTrialCount();
                		int successCount;
                		if (pendingSub.getSuccessCount() == null) {
                			successCount = 0;
                		} else {
                			successCount = pendingSub.getSuccessCount();
                		}
                		double successAvg = (double) successCount / (double) trialCount;
                		provisioning.setSuccessAverage(successAvg);
                	}
                	ChargeRequest req = new ChargeRequest();
                	req.setAmount(pendingProduct.getPrice().toString());
                	if (pendingSub.getMsisdn().startsWith("234")) {
                		String no = pendingSub.getMsisdn().replaceFirst("234", "0");
                		req.setMsisdn(no);
                	} else {
                		req.setMsisdn(pendingSub.getMsisdn());
                	}
                	req.setServiceName(pendingProduct.getTelcoServiceCode());
                	ChargeResponse restResponse = null;
                	boolean alreadySubscribed = false;
                	try {
                		List<Provisioning> provs = provisioningRepo
                				.findAllByProductIdAndContentIdAndMsisdnAndDateLogged(pendingProduct.getId(),
                						contents.get(0).getId(), pendingSub.getMsisdn(), date);
                		if (provs == null || provs.isEmpty()) {
                			provisioningRepo.save(provisioning);
                			req.setId(provisioning.getId());
                			restResponse = restEndpoint.postForObject(props.getChargingUrl(), req, ChargeResponse.class);
                		} else {
                			provisioning = provs.get(0);
                			alreadySubscribed = true;
                		}
                	} catch (Exception e) {
                		log.info(e.getMessage());
                	}

                	if (alreadySubscribed) {
                		response.text(pendingProduct.getMsgAlreadyJoined());
                	} else if (restResponse != null && restResponse.getCode() != null
                			&& restResponse.getCode().equals("303")) {  // billing successful
                		provisioning.setStatus("pending");
                		provisioning.setAmountCharged(pendingProduct.getPrice() / 100);
                		provisioning.setTrialCount(provisioning.getTrialCount() + 1);
                		provisioning.setNextRetrial(null);
                		//provisioningRepo.save(provisioning);
                		response.text(restResponse.getDescription());
                	} else {
                		Calendar instance = Calendar.getInstance();
                		if (provisioning.getStatus().equalsIgnoreCase("failed")) {
                			provisioning.setTrialCount(provisioning.getTrialCount() + 1);
                		} else {
                			provisioning.setStatus("failed");
                			provisioning.setTrialCount(1);
                		}
                		//instance.add(Calendar.HOUR, provisioning.getTrialCount());
                		instance.add(Calendar.MINUTE, provisioning.getTrialCount() * 10);
                		provisioning.setNextRetrial(instance.getTime());
                		provisioning.setAmountCharged(0.0);
                		//provisioningRepo.save(provisioning);
                		String res = restResponse != null ? restResponse.getDescription() : "Oops! Subscription failed please try again!";
                		response.text(res);
                	}
                }
//                if (!props.isChargeWhenSubscribe() || subSvc.chargeSubscriber(pendingSub, pendingProduct)) {
//                    // Send the welcome SMS here
//                    response.text(pendingProduct.getMsgWelcome());
//                    pendingSub.status(Subscriber.STATUS_PENDING);
//                    msisdnRepo.save(pendingSub);
//                } else {
//                    // Send failed to charge subscriber message
//                    response.text(pendingProduct.getMsgChargeFailed());
//                    pendingSub.status(Subscriber.STATUS_LEFT);
//                    msisdnRepo.save(pendingSub);
//                }
            }
        } else {
            // Try to opt-out first
            Product product = productRepo.findOneByLeftPatternIgnoreCase(shortMsg);
            log.debug("Found product for unsubscription: " + product);
            if (product != null) {
                // This one is an un-sub requests
                log.debug("Trying to unsubscribe user from product");
                smsReq.productId(product.getId());
                response.productId(product.getId());
                Subscriber sub = msisdnRepo.findOneByProductIdAndMsisdn(product.getId(), msisdn);
                if (sub == null) {
                    response.text(product.getMsgNotUsed());
                } else if (sub.getStatus() == Subscriber.STATUS_LEFT) {
                    response.text(product.getMsgAlreadyLeft());
                } else {
                    sub = msisdnSvc.unreg(sub);
                    // Delete pending SMS for this subscribers
                    smsMsgRepo.deleteByDestinationAndStatusLessThanAndProductId(msisdn, SmsLog.STATE_PENDING,
                            product.getId());
                    msisdnSvc.save(sub);
                    List<SmsContent> contents = msgSvc.getTodayContents(product);
                    LocalDate ld = LocalDate.now();
                    Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    Date date = Date.from(instant);
                    // Remove all of today's entries for this subscriber in the provisioning table
                    for (SmsContent content : contents) {
                        List<Provisioning> provs = provisioningRepo
                                .findAllByProductIdAndContentIdAndMsisdnAndDateLogged(product.getId(),
                                        content.getId(), sub.getMsisdn(), date);
                        for (Provisioning prov : provs) {
                            provisioningRepo.delete(prov);
                        }
                    }
                    response.text(product.getMsgFarewell());
                }
            } else {
                product = productRepo.findOneByJoinPatternIgnoreCase(shortMsg); // joinPattern
                log.debug("Lookup Subscription Keywords: " + product);
                // Now we try to opt-in customer
                if (product != null) {
                    smsReq.productId(product.getId());
                    response.productId(product.getId());
                    // First thing we request for confirmation by ask customer
                    // to send "Y" or 'YES" to our short code
                    Subscriber sub = msisdnRepo.findOneByProductIdAndMsisdn(product.getId(), msisdn);
                    log.debug("Found customer on this product: " + sub);
                    if ((sub != null) && (sub.getStatus() <= Subscriber.STATUS_PROCESSED)) {
                        response.text(product.getMsgAlreadyJoined());
                    } else if (props.isDoubleOptIn()) {
                        log.debug("== DOUBLE OPT-IN: " + msisdn);
                        if (sub == null) {
                            sub = new Subscriber().msisdn(msisdn).productId(product.getId());
                        }
                        sub.joinAt(ZonedDateTime.now())
                                .joinChannel("SMS")
                                .chargeNextTime(ZonedDateTime.now())
                                .status(Subscriber.STATUS_NEED_CONFIRM);
                        response.text(product.getDoubleOptinMsg() + " " + product.getCode());
                    } else {
                        log.debug("== SINGLE OPTIN: " + msisdn);
                        if (sub == null) {
                            sub = new Subscriber().msisdn(msisdn).productId(product.getId());
                        }
                        sub.joinAt(ZonedDateTime.now())
                                .joinChannel("SMS")
                                .status(Subscriber.STATUS_PENDING)
                                .chargeNextTime(ZonedDateTime.now());
                        if (!props.isChargeWhenSubscribe() || subSvc.chargeSubscriber(sub, product)) {
                            // Send the welcome SMS here
                            response.text(product.getMsgWelcome());
                            sub.status(Subscriber.STATUS_PENDING);
                        } else {
                            // Send failed to charge subscriber message
                            response.text(product.getMsgChargeFailed());
                            sub.status(Subscriber.STATUS_LEFT);
                        }
                    }
                    msisdnSvc.save(sub);
                } else {
                    // Send the invalid syntax
                    log.debug("Sorry we didn't recognize your keywords. Text HELP for more info.");
                    response.text(productTpl.getMsgWelcome());
                }
            }
        }
        // Send message back
        log.debug("Response to customer: " + response);
        if (response.getText() != null) {
            smsRepo.save(response);
        }
        smsRepo.save(smsReq);
    }

    public void processDLR(String valueAsString) {
        SmsLog sms = smsMsgRepo.findOneByMessageId(valueAsString);
        if (sms != null) {
            smsMsgRepo.save(sms.deliveredAt(ZonedDateTime.now()).status(SmsLog.STATE_DELIVERED));
        }
    }
}
