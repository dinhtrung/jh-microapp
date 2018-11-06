package com.ft.component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.ft.config.ApplicationProperties;
import com.ft.config.SmsMtConfig;
import com.ft.domain.Cdr;
import com.ft.domain.Product;
import com.ft.domain.Provisioning;
import com.ft.domain.Sms;
import com.ft.domain.SmsContent;
import com.ft.domain.Subscriber;
import com.ft.repository.CdrRepository;
import com.ft.repository.ProvisioningRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SmsRepository;
import com.ft.repository.SubscriberRepository;
import com.ft.service.SmsContentService;
import com.ft.service.dto.ChargingProfile;
import com.ft.web.api.model.ChargeRequest;
import com.ft.web.api.model.ChargeResponse;

import java.util.ArrayList;
import java.util.Calendar;

public class ChargeProductCallable implements Callable<List<Cdr>> {

    private final Logger log = LoggerFactory.getLogger(ChargeProductCallable.class);

    private final ApplicationProperties props;
    private final Provisioning provisioning;
    private final Subscriber subscriber;
    private final Product product;
    private final List<SmsContent> contents;
    private final CdrRepository cdrRepository;
    private final SmsLogRepository smsLogRepo;
    private final SmsRepository smsRepo;
    private final RestTemplate restTemplate;
    /**
     * Charging Client *
     */
    private final SmsMtConfig mtsender;
    private final SmsContentService msgSvc;
    private final ProvisioningRepository provisioningRepo;
    private final SubscriberRepository subscriberRepo;

    /**
     * Perform a charge on a separate thread
     *
     * @param provisioning
     * @param subscriber
     * @param product	: Product
     * @param contents
     * @param cdrRepository	: For CDR saving
     * @param subscriptionRepository
     * @param smsRepo
     * @param mtsender
     * @param msgSvc
     * @param smsLogRepo	: For SMS sending
     * @param subscriberRepo
     * @param restEndpoint
     */
    public ChargeProductCallable(
    		ApplicationProperties props,
    		Provisioning provisioning,
    		Subscriber subscriber,
    		Product product,
            List<SmsContent> contents,
            CdrRepository cdrRepository,
            ProvisioningRepository subscriptionRepository,
            SmsLogRepository smsLogRepo,
            SmsRepository smsRepo,
            SmsContentService msgSvc,
            SmsMtConfig mtsender,
            SubscriberRepository subscriberRepo,
            RestTemplate restEndpoint
            ) {
        super();
        this.props = props;
        this.provisioning = provisioning;
        this.subscriber = subscriber;
        this.product = product;
        this.contents = contents;
        this.cdrRepository = cdrRepository;
        this.provisioningRepo = subscriptionRepository;
        this.smsRepo = smsRepo;
        this.smsLogRepo = smsLogRepo;
        this.mtsender = mtsender;
        this.msgSvc = msgSvc;
        this.subscriberRepo = subscriberRepo;
        this.restTemplate = restEndpoint;
    }

    @Override
    public List<Cdr> call() throws Exception {
        List<Cdr> requests = new ArrayList<>();
        try {
            Optional<SmsContent> contentOpt = msgSvc.findOne(provisioning.getContentId());
            // fallback
           	SmsContent content = contentOpt.isPresent() ? contentOpt.get() : msgSvc.getTodayContents(product).get(0);
            ZonedDateTime now = ZonedDateTime.now();
            for (ChargingProfile profile : product.getChargingProfiles()) {
                subscriber.setTrialCount(subscriber.getTrialCount() != null ? subscriber.getTrialCount() + 1 : 1);
                Cdr transaction = new Cdr()
                        .productId(product.getId())
                        .contentId(content.getId())
                        .msisdn(subscriber.getMsisdn())
                        .requestAt(ZonedDateTime.now())
                        .requestPayload(profile.getMessage())
                        .amount(profile.getChargePrice().doubleValue());

                if (subscriber.getPartnerCode() != null) {
                    transaction.setPartnerCode(subscriber.getPartnerCode());
                }
                ChargeRequest req = new ChargeRequest();
                req.setAmount(product.getPrice().toString());
                req.setId(provisioning.getId());
                if (subscriber.getMsisdn().startsWith("234")) {
                    String no = subscriber.getMsisdn().replaceFirst("234", "0");
                    req.setMsisdn(no);
                } else {
                    req.setMsisdn(subscriber.getMsisdn());
                }
                req.setServiceName(product.getTelcoServiceCode());
                ChargeResponse resp = restTemplate.postForObject(props.getChargingUrl(), req, ChargeResponse.class);
                transaction
                .requestPayload(req)
                .responsePayload(resp);
                //if (resp != null && resp.getCommandStatus() == 0) {  // billing successful
                if (resp != null && resp.getCode() != null && resp.getCode().equals("200")) {  // billing successful
                    // I will comment out the whole of this SMS provisioning block
                    // since the success response here only means successfully delivered to
                    // the telco's endpoint Async. We will wait for the actual response later
//                    try {
//                        if (provisioning.getBillPerSms()) {
//                            smsLogRepo.save(
//                                    new SmsLog()
//                                    .source(product.getBroadcastShortcode())
//                                    .destination(subscriber.getMsisdn())
//                                    .text(content.getMessage())
//                                    .contentId(content.getId())
//                                    .productId(product.getId())
//                                    .submitAt(content.getStartAt())
//                                    .status(SmsLog.STATE_PENDING)
//                            );
//                        } else {
//                            for (SmsContent sms : contents) {
//                                smsLogRepo.save(
//                                        new SmsLog()
//                                        .source(product.getBroadcastShortcode())
//                                        .destination(subscriber.getMsisdn())
//                                        .text(sms.getMessage())
//                                        .contentId(sms.getId())
//                                        .productId(product.getId())
//                                        .submitAt(sms.getStartAt())
//                                        .status(SmsLog.STATE_PENDING)
//                                );
//                            }
//                        }
//                    } catch (Exception e) {
//                        log.error("Cannot save SMS " + content.getMessage() + " --> " + subscriber.getMsisdn(), e);
//                    }
//                    transaction
//                            .status(true)
//                            .responsePayload(String.valueOf(resp.getCommandStatus()))
//                            .note(resp.getMessageId())
//                            .responseAt(ZonedDateTime.now());
//                    cdrRepository.save(transaction);

                    // update subscriber
                    //subscriber.setSuccessCount(subscriber.getSuccessCount() != null ? subscriber.getSuccessCount() + 1 : 1);
                    //subscriber.setChargeLastSuccess(now);
                    provisioning.setStatus("pending");
                    provisioning.setAmountCharged(profile.getChargePrice().doubleValue());
                    provisioning.setTrialCount(provisioning.getTrialCount() + 1);
                    provisioning.setNextRetrial(null);
                    provisioningRepo.save(provisioning);

                    requests.add(transaction);
                    //subscriberRepo.save(subscriber);
                    break;
                } else {
                    transaction
                            .status(false)
                            .amount(0.0)
                            .responsePayload(resp)
                            .note(resp != null ? resp.getTranxId() : "")
                            .responseAt(ZonedDateTime.now());
                    cdrRepository.save(transaction);
//                    billingRequest.setAmountCharged(0.0);
//                    billingRequest.setResponseStatus("failed");
//                    requests.add(billingRequest);
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
                    provisioningRepo.save(provisioning);
                    if ((provisioning.getNotifyOnFailedBilling() != null) && provisioning.getNotifyOnFailedBilling()) {
                        smsRepo.save(new Sms()
                                .source(product.getBroadcastShortcode())
                                .destination(subscriber.getMsisdn())
                                .text(product.getMsgChargeFailed())
                                .productId(product.getId())
                                .submitAt(ZonedDateTime.now())
                                .status(Sms.STATE_PENDING)
                        );
                    }
                }
                subscriberRepo.save(subscriber);
            }

        } catch (Exception e) {
            log.error("Exception (33): " + e.getMessage() + " --> ", e);
        }
        return requests;
    }

    /**
     * 0x00000000 0 Success 0x0000000A 10 Invalid Source Address. 0x0000000B 11
     * Invalid Dest Addr. 0x00000064 100 The mobile phone number does not exist.
     * 0x0000006A 106 Subscriber Invalid 0x000001F9 505 System internal error.
     * 0x000003E9 1001 Subscriber does not Exist on MDSP(sub has not been
     * provisioned to use MDSP related services) 0x000003ED 1005 SCP operation
     * failure(Subscriber has been barred) 0x00000411 1041 Maximum Submission
     * Number Exceeded 0x00000412 1042 Maximum Delivery Number Exceeded
     * 0x00000552 1362 Operation Time Out 0x0000055F 1375 SP level request rate
     * control not pass 0x00000C1D 3101 Insufficient Balance 0x00000C24 3108 The
     * number is suspended
     */
    public final List<Integer> severeErrors = Arrays.asList(11, 100, 106, 1001, 1005, 3108);

}
