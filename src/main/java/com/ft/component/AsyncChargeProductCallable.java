package com.ft.component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.config.ApplicationProperties;
import com.ft.domain.Cdr;
import com.ft.domain.Product;
import com.ft.domain.SmsContent;
import com.ft.domain.Subscriber;
import com.ft.repository.CdrRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SmsRepository;
import com.ft.repository.SubscriberRepository;
import com.ft.service.dto.ChargingProfile;
import com.ft.web.api.model.ChargeRequest;
import com.ft.web.api.model.ChargeResponse;

import io.github.bucket4j.Bucket;

public class AsyncChargeProductCallable implements Callable<Integer> {

    private final Logger log = LoggerFactory.getLogger(AsyncChargeProductCallable.class);

    private final List<Subscriber> msisdnList;
    private final Product product;
    private final List<SmsContent> contents;
    private final CdrRepository cdrRepository;
    private final SubscriberRepository msisdnRepo;
    private final SmsLogRepository smsRepo;
    private final SmsRepository respRepo;
    private final ApplicationProperties props;
    /**
     * Charging Client *
     */
    private final ObjectMapper mapper;
    private final RestTemplate chargingRestTemplate;
    private final Bucket bucket;

    /**
     * Perform a charge on a separate thread
     *
     * @param msisdnList : List of MSISDN to be charged.
     * @param product	: Product
     * @param contents
     * @param cdrRepository	: For CDR saving
     * @param respRepo
     * @param props
     * @param bucket
     * @param restTemplate
     * @param subMsisdnRepository	: For MSISDN update
     * @param mapper
     * @param smsRepo	: For SMS sending
     */
    public AsyncChargeProductCallable(List<Subscriber> msisdnList, Product product, List<SmsContent> contents,
            CdrRepository cdrRepository, SubscriberRepository subMsisdnRepository, SmsLogRepository smsRepo,
            SmsRepository respRepo, ApplicationProperties props, Bucket bucket, RestTemplate restTemplate,
            ObjectMapper mapper) {
        super();
        this.msisdnList = msisdnList;
        this.product = product;
        this.contents = contents;
        this.cdrRepository = cdrRepository;
        this.msisdnRepo = subMsisdnRepository;
        this.respRepo = respRepo;
        this.smsRepo = smsRepo;
        this.props = props;
        this.chargingRestTemplate = restTemplate;
        this.bucket = bucket;
        this.mapper = mapper;
        //log.info("== THREAD " + this.hashCode() + ": Charge " + msisdnList.size() + " customers on product [" + product.getCode() + "] for " + contents.size() + " messages");
    }

    @Override
    public Integer call() throws Exception {

        Integer requestCnt = 0;
        int successCnt = 0;
        for (Subscriber subscriber : msisdnList) {
            subscriber.chargeLastTime(ZonedDateTime.now());
            try {
                requestCnt++;
                if ((requestCnt % 50) == 0) {
                    log.info("=== THREAD " + this.hashCode() + ": Submitted " + requestCnt + " requests on product [" + product.getCode() + "] for " + contents.size() + " messages");
                }
                while (!bucket.tryConsume(1)) {
                    Thread.sleep(RandomUtils.nextInt(0, 5000));
                }
                if (props.isChargePerSms()) {
                    for (SmsContent content : contents) {
                        Cdr result = this.doCharge(subscriber, content);
                        if ((result != null) && result.isStatus()) {
                            successCnt++;
                        }
                    }
                } else {
                    Cdr result = this.doCharge(subscriber);
                    if ((result != null) && result.isStatus()) {
                        successCnt++;
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("Cannot perform charge request for subscriber " + subscriber);
                e.printStackTrace();
            }

        }
        log.info("== THREAD: " + this.hashCode() + ": Charge " + successCnt + " / " + requestCnt + "  from " + msisdnList.size() + " customers on product [" + product.getCode() + "] for " + contents.size() + " messages");
        return requestCnt;
    }

    /**
     * 0x00000000 0 Success 0x0000000A 10 Invalid Source Address. 0x0000000B 11
     * Invalid Dest Addr.
     */
    public final List<String> severeErrors = Arrays.asList(
            "com.mcentric.gateway.NotImplementedException", // Not available for POSTPAID Subscribers
            "com.mcentric.gateway.in.AccountNotFoundException",
            "com.mcentric.gateway.NodeNotFoundException"
    );

    /**
     * Etisalat Charging Method - Charge Per SMS
     *
     * @param subscriber
     * @return
     */
    public Cdr doCharge(Subscriber subscriber, SmsContent content) {
        for (ChargingProfile profile : product.getChargingProfiles()) {
            Cdr transaction = cdrRepository.save(new Cdr()
                    .productId(product.getId())
                    .partnerCode(product.getPartnerCode())
                    .contentId(content.getId())
                    .msisdn(subscriber.getMsisdn())
                    .requestAt(ZonedDateTime.now())
                    .amount(profile.getChargePrice().doubleValue())
            //					.note(note)
            );
            // Create the Charge Requests
            ChargeRequest request = new ChargeRequest();
            request.setAmount(transaction.getAmount().toString());
            request.setId(transaction.getId());
            request.setMsisdn("0" + transaction.getMsisdn().substring(props.getMcc().length()));
            request.setServiceName(profile.getShortCode());
            // FIXME: SHould I leave the Shortcode as the one to store data
            try {
                log.info("REQUEST: " + request);
                ResponseEntity<ChargeRequest> resp = chargingRestTemplate.postForEntity(props.getChargingUrl(), request, ChargeRequest.class);
                return cdrRepository.save(transaction.status(false).requestPayload(request.toString()).note(resp.toString()));
//				log.debug("SUCCESS CHARGE: " + subscriber);
//				int affected = msisdnRepo.updateSuccessStatus(subscriber.getMsisdn());
//				log.debug("PRIORITIZE " + affected + " documents ---> " + Subscriber.STATUS_PENDING);
//				// Schedule the customer to be charge next time
//				msisdnRepo.save(
//						subscriber
//						.status(Subscriber.STATUS_PROCESSED)
//						.notify(Subscriber.NOTIFY_PENDING)
//						.chargeLastSuccess(ZonedDateTime.now())
//						.chargeNextTime(ZonedDateTime.now().plusDays(profile.getChargePeriod()).withHour(1))
//						.expiryTime(ZonedDateTime.now().plusDays(profile.getChargePeriod()))
//					);
//					log.debug("SUCCESS CHARGE: " + subscriber);
//
//					smsRepo.save(
//							new SmsLog()
//							.source(product.getBroadcastShortcode())
//							.destination(subscriber.getMsisdn())
//							.text(content.getMessage())
//							.contentId(content.getId())
//							.productId(product.getId())
//							.submitAt(content.getStartAt())
//							.status(SmsLog.STATE_PENDING)
//							);
//				// Save the CDR
//				transaction
//					.status(true)
//					.note(resp)
//					.responseAt(ZonedDateTime.now());
//				return cdrRepository.save(transaction);
            } catch (HttpStatusCodeException s) {
                log.error("CANNOT SUBSCRIBE FOR DELIVERY REPORT");
                log.error("== HTTP CODE: " + s.getRawStatusCode() + " - " + s.getStatusText() + ": " + s.getResponseBodyAsString());
                List errorResponse;
                try {
                    errorResponse = mapper.readValue(s.getResponseBodyAsString(), List.class);
                } catch (Exception e) {
                    log.error("Found Unknown Error");
                    errorResponse = Arrays.asList("APP", e.getMessage());
                }
                if (severeErrors.contains(errorResponse.get(0).toString())) {
                    // Invalid customer or something? Delete him.
                    msisdnRepo.delete(subscriber);
                    break;
                } else {
                    transaction
                            .status(false)
                            .amount(0.0)
                            .note(errorResponse.toString())
                            .responseAt(ZonedDateTime.now());
                    cdrRepository.save(transaction);
                }
            } catch (Exception e) {
                log.error("CANNOT SUBSCRIBE FOR DELIVERY REPORT");
                e.printStackTrace();
            }
        }
//		int affected = msisdnRepo.updateFailedStatus(subscriber.getMsisdn(), failedStatus );
//		log.debug("FAILED status for " + affected + " documents ---> " + failedStatus);
//		// We failed to charge no matter what... just inform customer
//		if (props.isNotifyChargeFailed()) respRepo.save(new Sms()
//				.source(product.getBroadcastShortcode())
//				.destination(subscriber.getMsisdn())
//				.text(product.getMsgChargeFailed())
//				.productId(product.getId())
//				.submitAt(ZonedDateTime.now())
//				.status(Sms.STATE_PENDING)
//		);
        return null;
    }

    /**
     * Etisalat Charging Method - Charge Per Date
     *
     * @param subscriber
     * @return
     */
    public Cdr doCharge(Subscriber subscriber) {
        for (ChargingProfile profile : product.getChargingProfiles()) {
            Cdr transaction = cdrRepository.save(new Cdr()
                    .productId(product.getId())
                    .partnerCode(product.getPartnerCode())
                    .msisdn(subscriber.getMsisdn())
                    .requestAt(ZonedDateTime.now())
                    .requestPayload(profile.getMessage())
                    .amount(profile.getChargePrice().doubleValue())
            //					.note(note)
            );
            // Check account balance

            // Create the Charge Requests
            ChargeRequest request = new ChargeRequest();
            request.setAmount(transaction.getAmount().toString());
            request.setId(transaction.getId());
            request.setMsisdn("0" + transaction.getMsisdn().substring(props.getMcc().length()));
            request.setServiceName(profile.getShortCode());
            try {
                ResponseEntity<ChargeResponse> resp = chargingRestTemplate.postForEntity(props.getChargingUrl(), request, ChargeResponse.class);
                return cdrRepository.save(transaction.status(false));
            } catch (HttpStatusCodeException s) {
                log.error("CANNOT CHARGE CUSTOMER " + request);
                log.error("== HTTP CODE: " + s.getRawStatusCode() + " - " + s.getStatusText() + ": " + s.getResponseBodyAsString());
                List errorResponse;
                try {
                    errorResponse = mapper.readValue(s.getResponseBodyAsString(), List.class);
                } catch (Exception e) {
                    log.error("Found Unknown Error");
                    errorResponse = Arrays.asList("APP", e.getMessage());
                }
                if (severeErrors.contains(errorResponse.get(0).toString())) {
                    // Invalid customer or something? Delete him.
                    msisdnRepo.delete(subscriber);
                    break;
                } else {
                    transaction
                            .status(false)
                            .amount(0.0)
                            .note(String.valueOf(errorResponse.get(0)))
                            .note(String.valueOf(errorResponse.get(1)))
                            .responseAt(ZonedDateTime.now());
                    cdrRepository.save(transaction);
                }
            } catch (ResourceAccessException e) {
                log.error("FAILED TO CONNECT TO ENDPOINT: " + e.getMessage());
                // TODO: Temporary disable service and retry
                return null;
            } catch (Exception e) {
                log.error("CANNOT CHARGE: " + request);
                e.printStackTrace();
            }
        }
        int failedStatus = (subscriber.getStatus() > 0) ? -1 : subscriber.getStatus() - 1;
        long affected = msisdnRepo.updateFailedStatus(subscriber.getMsisdn(), failedStatus);
        log.debug("FAILED status for " + affected + " documents ---> " + failedStatus);
        return null;
    }
}
