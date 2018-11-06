package com.ft.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.ft.component.ChargeProductCallable;
import com.ft.config.ApplicationProperties;
import com.ft.config.SmsMtConfig;
import com.ft.domain.Cdr;
import com.ft.domain.Product;
import com.ft.domain.Provisioning;
import com.ft.domain.ProvisioningStat;
import com.ft.domain.Sms;
import com.ft.domain.SmsContent;
import com.ft.domain.SmsLog;
import com.ft.domain.Subscriber;
import com.ft.repository.CdrRepository;
import com.ft.repository.DndRepository;
import com.ft.repository.ProvisioningRepository;
import com.ft.repository.ProvisioningStatRepository;
import com.ft.repository.SmsContentRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SmsRepository;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;
import com.ft.service.dto.ChargingProfile;
import com.ft.service.util.SmsSender;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static java.lang.Math.toIntExact;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@EnableScheduling
public class SubscriptionService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    private ApplicationProperties props;

    @Autowired
    private SmsContentService msgSvc;

    @Autowired
    private SubProductService productSvc;

    @Autowired
    private SmsLogRepository smsLogRepo;

    @Autowired
    private SmsRepository smsRepo;

    @Autowired
    private SubscriberRepository subscriberRepo;

    @Autowired
    private DndRepository dndRepo;

    @Autowired
    private SubProductRepository prodRepo;

    @Autowired
    private SmsContentRepository msgRepo;

    @Autowired
    private CdrRepository cdrRepo;

    @Autowired
    private ProvisioningRepository provisioningRepo;

    @Autowired
    private SmsMtConfig mtsender;
    @Autowired
    private RestTemplate restEndpoint;

    @Autowired
    SmsSender smsSender;

    @Autowired
    private ProvisioningStatRepository provisioningStatRepo;

    /**
     * Send a Provisioning request
     *
     * @param msisdn
     * @param product
     * @return
     */
    public boolean subscribe(String msisdn, String product) {
        // TODO: Implement logic here
        return false;
    }

    /**
     * Send a UnSubscription request
     *
     * @param msisdn
     * @param product
     * @return
     */
    public boolean unsub(String msisdn, String product) {
        return unsub(subscriberRepo.findOneByMsisdnAndProductId(msisdn, product));
    }

    /**
     * Reset subscriber status everyday
     *
     * @return
     */
    @Scheduled(cron = "0 0 0 * * *")
    public long resetSubscribers() {
        long result = subscriberRepo.resetActiveSubscriberStatus();
        log.info("=== RESET: " + result + " subscribers");
        return result;
    }

    /**
     * Trying to renew customer
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 60000)
    public int renew() throws InterruptedException, ExecutionException {
        List<Product> products = productSvc.findAll();
        if (products == null || products.isEmpty()) {
            // no product found. STOP!
            return 0;
        }
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(700);
        List<Future<List<Cdr>>> futures = new ArrayList<>();
        LocalDate ld = LocalDate.now();
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date today = Date.from(instant);
        List<String> statuses = new ArrayList<>();
        statuses.add("fresh");
        statuses.add("failed");

        Page<Provisioning> provs = provisioningRepo
                .findAllByStatusInAndDateLoggedEqualsAndNextRetrialBeforeOrderByTrialCount(
                        statuses, today, new Date(), PageRequest.of(0, 900));
        List<Provisioning> provisionings = provs.getContent();
        log.info("=== Total subscriptions found (fresh or failed) @  " + (new Date()) + " ==   "
               + provisionings.size());
        int subscriptionCount = 0;
        for (Provisioning provisioning : provisionings) {
            Subscriber subscriber = subscriberRepo.findOneByMsisdnAndProductId(
                    provisioning.getMsisdn(), provisioning.getProductId());
            //SmsContent content = msgSvc.findOne(subscription.getContentId());
            Optional<Product> productOpt = productSvc.findOne(provisioning.getProductId());
            if (productOpt.isPresent()) {
            	Product product = productOpt.get();
            	List<SmsContent> contents = msgSvc.getTodayContents(product);
            	Future<List<Cdr>> future = executor.submit(new ChargeProductCallable(props, provisioning, subscriber, product,
            			contents, cdrRepo, provisioningRepo, smsLogRepo, smsRepo,
            			msgSvc, mtsender, subscriberRepo, restEndpoint));
            	futures.add(future);
            	subscriptionCount = subscriptionCount + 1;
            }
        }
        log.info("  === Total subscriptions submitted  @  " + (new Date()) + " ==   " + subscriptionCount);
        debug(futures);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();
        long diff = end - start;
        log.info(" Total execution time for " + subscriptionCount + " subscriptions is:   " + (diff / 1000) + "sec");
        if (diff < 60000 && subscriptionCount != 0) {
            Thread.sleep(60000 - diff);
            log.info(" Thread slept for " + ((60000 - diff) / 1000) + "sec");
        }
        return 0;
    }

    private void debug(List<Future<List<Cdr>>> futures) {
        if (futures == null || futures.isEmpty()) {
            return;
        }
        for (Future<List<Cdr>> fut : futures) {
            try {
                if (fut == null) {
                    log.error("===  Null future  SubscriptionService#debug()  ===  ");
                } else {
                    List<Cdr> requests = fut.get();
                    if (requests != null) {
                        for (Cdr req : requests) {
                            if (req != null) {
                                cdrRepo.save(req);
                            }
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("SubscriptionService#debug() --> Exception: ", e.getMessage());
            } catch (Exception ex) {
                log.error("SubscriptionService#debug() --> Exception 2: ", ex.getMessage());
            }
        }
    }

    /**
     * Send Pre-charge notification to customer Pre-charge notification should
     * be sent before actually charge one day.
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 600000)
    public int prechargeNotification() throws InterruptedException, ExecutionException {
        int result = 0;
        if (props.isPreChargeNotify()) {
            for (Product prod : productSvc.findAll()) {
                // Retrieve the pending subscribers
                List<Subscriber> subList = subscriberRepo.findAllByProductIdAndNotifyBetweenAndStatusBetweenAndChargeNextTimeLessThanOrderByStatusDesc(prod.getId(), Subscriber.NOTIFY_EXPIRED, Subscriber.NOTIFY_PROCESSED, Subscriber.STATUS_EXPIRED, Subscriber.STATUS_LEFT, ZonedDateTime.now().plusDays(1));
                log.info("=== Sending pre-charge notification for " + subList.size() + " customers");
                for (Subscriber sub : subList) {
                    result++;
                    smsRepo.save(new Sms(props.getSms())
                            .source(prod.getBroadcastShortcode())
                            .submitAt(ZonedDateTime.now())
                            .destination(sub.getMsisdn())
                            .text(prod.getMsgPrecharge())
                            .status(SmsLog.STATE_PENDING));
                    subscriberRepo.save(sub
                            .notify(Subscriber.NOTIFY_DONE)
                            .notifyLastTime(ZonedDateTime.now())
                            .status(Subscriber.STATUS_PENDING));
                }
            }
        }
        return result;
    }

    /**
     * Provide API for web subscription
     *
     * @param msisdn
     * @param keyword
     * @param shortCode
     * @param channel
     * @param partner
     * @param subType
     * @param notify
     * @return
     */
    public ResponseEntity<String> actionApi(String msisdn, String keyword, String shortCode,
            String channel, String partner, String subType, Integer notify) {
        keyword = keyword.trim().toUpperCase();
        if (dndRepo.findById(msisdn).isPresent()) {
            return ResponseEntity.badRequest().body("You are in DND list");
        }
        Product product = prodRepo.findOneByJoinPatternIgnoreCase(keyword);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        // Here come the flow...
        Subscriber subscriber = subscriberRepo.findOneByProductIdAndMsisdn(product.getId(), msisdn);
        if (subscriber == null) {
            subscriber = new Subscriber().msisdn(msisdn).productId(product.getId());
            subscriber.setTrialCount(0);
            subscriber.setSuccessCount(0);
        }
        Sms sms = new Sms(props.getSms())
                .source(product.getBroadcastShortcode())
                .submitAt(ZonedDateTime.now())
                .destination(msisdn)
                .status(SmsLog.STATE_PENDING);
        if (subType.equalsIgnoreCase("SUB")) {
            subscriber.setJoinAt(ZonedDateTime.now());
            subscriber.setJoinChannel(channel);
            subscriber.setPartnerCode(partner);
            subscriber.setStatus(0);
            sms.text(product.getMsgWelcome());

            try {
                LocalDate ld = LocalDate.now();
                Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Date date = Date.from(instant);
                Provisioning provisioning;
                List<SmsContent> contents = msgSvc.getTodayContents(product);
                if (contents == null || contents.isEmpty()) {
                    log.info("=== No contents found for product " + product.getCode()
                            + " for new subscriber @  " + (new Date()));
                } else if (props.isChargePerSms()) {
                    for (SmsContent content : contents) {
                        provisioning = new Provisioning();
                        if (props.isNotifyChargeFailed()) {
                            provisioning.setNotifyOnFailedBilling(true);
                        }
                        if (props.isPreChargeNotify()) {
                            provisioning.setNotifyBeforeBilling(true);
                        }
                        provisioning.setBillPerSms(true);
                        provisioning.setProductId(product.getId());
                        provisioning.setContentId(content.getId());
                        provisioning.setMsisdn(subscriber.getMsisdn());
                        provisioning.setStatus("fresh");
                        provisioning.setTrialCount(0);
                        provisioning.setDateLogged(date);
                        provisioning.setNextRetrial(new Date());
                        provisioningRepo.save(provisioning);
                    }
                } else {
                    provisioning = new Provisioning();
                    if (props.isNotifyChargeFailed()) {
                        provisioning.setNotifyOnFailedBilling(true);
                    }
                    if (props.isPreChargeNotify()) {
                        provisioning.setNotifyBeforeBilling(true);
                    }
                    SmsContent content = contents.get(0);
                    provisioning.setBillPerSms(false);
                    provisioning.setProductId(product.getId());
                    provisioning.setContentId(content.getId());
                    provisioning.setMsisdn(subscriber.getMsisdn());
                    provisioning.setStatus("fresh");
                    provisioning.setTrialCount(0);
                    provisioning.setDateLogged(date);
                    provisioning.setNextRetrial(new Date());
                    try {
                        provisioningRepo.save(provisioning);
                    } catch (Exception e) {
                    }
                }
                log.info(" Subcriber  " + subscriber.getMsisdn() + " provisioned for product "
                        + product.getCode() + "  @  " + (new Date()));
            } catch (Exception e) {
                log.info("Error provisioning new subscribers:  " + e.getMessage());
            }

        } else if (subType.equalsIgnoreCase("UNSUB")) {
            if (subscriber.getStatus() == null) {
                sms.text(product.getMsgNotUsed());
            } else if (subscriber.getStatus() == 2) {
                sms.text(product.getMsgAlreadyLeft());
            } else {
                sms.text(product.getMsgFarewell());
                subscriber.setLeftAt(ZonedDateTime.now());
                subscriber.setStatus(2);
                subscriber.setLeftChannel(channel);
                // FIXME: Delete all pending SMS
                smsLogRepo.deleteByDestinationAndSubmitAtGreaterThan(msisdn, ZonedDateTime.now());
            }
        }
        // Only skip notify if `notify=0`
        if (notify != 0) {
            smsRepo.save(sms);
        }
        subscriberRepo.save(subscriber);
        return ResponseEntity.accepted().body("OK");
    }

    /**
     * Remove old un-chargable customers
     *
     * @return
     */
    @Scheduled(cron = "0 0 0 * * *")
    public int cleanupSuspendedSubscribers() {
        return subscriberRepo.deleteAllByChargeLastSuccessIsNullOrChargeLastSuccessLessThan(ZonedDateTime.now().minusMonths(3));
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
    public final List<Integer> severeErrors = Arrays.asList(11, 100, 106, 1001, 1005);

    /**
     * Charge the customer when he first join our product
     *
     * Loop through all available charging status to charge him.
     *
     * @param subscriber
     * @param product
     * @return
     */
    public boolean chargeSubscriber(Subscriber subscriber, Product product) {
        for (ChargingProfile profile : product.getChargingProfiles()) {
            try {
                Cdr transaction = new Cdr()
                        .productId(product.getId())
                        .partnerCode(product.getPartnerCode())
                        .msisdn(subscriber.getMsisdn())
                        .requestAt(ZonedDateTime.now())
                        .requestPayload(profile.getMessage())
                        .amount(profile.getChargePrice().doubleValue());
                SubmitSmResp resp = mtsender.chargeMessage(profile.getShortCode(), product.getMsgRenew(), subscriber.getMsisdn());
                if (resp.getCommandStatus() == 0) {
                    log.debug("SUCCESS CHARGE: " + subscriber);
                    long affected = subscriberRepo.updateSuccessStatus(subscriber.getMsisdn());
                    log.debug("PRIORITIZE " + affected + " documents ---> " + Subscriber.STATUS_PENDING);
                    // Schedule the customer to be charge next time
                    subscriberRepo.save(subscriber
                            .status(Subscriber.STATUS_PROCESSED)
                            .notify(Subscriber.NOTIFY_PENDING)
                            .chargeLastSuccess(ZonedDateTime.now())
                            .chargeNextTime(ZonedDateTime.now().plusDays(profile.getChargePeriod()).withHour(1))
                    );
                    log.debug("SUCCESS CHARGE: " + subscriber);
                    // Send Welcome SMS
                    if (profile.getMsgWelcome() != null) {
                        smsRepo.save(
                                new Sms()
                                .source(product.getBroadcastShortcode())
                                .destination(subscriber.getMsisdn())
                                .text(profile.getMsgWelcome())
                                .productId(product.getId())
                                .submitAt(ZonedDateTime.now())
                                .status(SmsLog.STATE_PENDING)
                        );
                    }

                    // FIXME: Create the SMS - Should we do this now, or should we let the Content Prepare process do it automatically
                    for (SmsContent content : msgSvc.getToday(product.getId())) {
                        try {
                            smsLogRepo.save(
                                    new SmsLog()
                                    .source(product.getBroadcastShortcode())
                                    .destination(subscriber.getMsisdn())
                                    .text(content.getMessage())
                                    .contentId(content.getId())
                                    .productId(product.getId())
                                    .submitAt(content.getStartAt())
                                    .status(SmsLog.STATE_PENDING)
                            );
                        } catch (Exception e) {
                            log.error("Cannot save SMS: " + content.getMessage() + " --> " + subscriber.getMsisdn(), e);
                        }
                    }
                    // Save the CDR
                    transaction
                            .status(true)
                            .responsePayload(String.valueOf(resp.getCommandStatus()))
                            .note(resp.getMessageId())
                            .responseAt(ZonedDateTime.now());
                    cdrRepo.save(transaction);
                    return true;
                } else if (severeErrors.contains(resp.getCommandStatus())) {
                    // Invalid customer or something? Delete him.
                    subscriberRepo.delete(subscriber);
                    break;
                } else {
                    int failedStatus = (subscriber.getStatus() > 0) ? -1 : subscriber.getStatus() - 1;
                    long affected = subscriberRepo.updateFailedStatus(subscriber.getMsisdn(), failedStatus);
                    log.debug("FAILED status for " + affected + " documents ---> " + failedStatus);
                    transaction
                            .status(false)
                            .amount(0.0)
                            .responsePayload(String.valueOf(resp.getCommandStatus()))
                            .note(resp.getMessageId())
                            .responseAt(ZonedDateTime.now());
                    cdrRepo.save(transaction);
                }
            } catch (Exception e) {
                log.error("Cannot charge subscriber " + subscriber.getMsisdn() + " -- " + product.getCode(), e);
            }
        }
        return false;
    }

    public boolean unsub(Subscriber sub) {
        // Update customer status
        try {
            subscriberRepo.save(sub.status(Subscriber.STATUS_LEFT).leftAt(ZonedDateTime.now()));
            smsLogRepo.deleteByDestinationAndStatusLessThanAndProductId(sub.getMsisdn(), SmsLog.STATE_SUBMITTED, sub.getProductId());
            return true;
        } catch (Exception e) {
            log.error("Cannot unsubscribe customer " + sub, e);
        }
        return false;
    }

    /**
     * This method very important and should be altered with care. Please refer
     * to the PDF doc for information about the logic behind what this method
     * does.
     */
    @Scheduled(fixedDelay = 3600000)
    public int provisionSubscribers() throws InterruptedException, ExecutionException {
        List<Product> products = productSvc.findAll();
        if (products == null || products.isEmpty()) {
            // no product found. STOP!
            return 0;
        }
        try {
            log.info("==== Total products found " + "  @  " + (new Date())
                    + " === " + products.size());
            List<SmsContent> contents;
            List<Subscriber> subscribers;
            Provisioning provisioning;
            LocalDate ld = LocalDate.now();
            Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            for (Product prod : products) {
                contents = msgSvc.getTodayContents(prod);
                if (contents == null || contents.isEmpty()) {
                    log.info("=== No contents found for product " + prod.getCode() + "  @  " + (new Date()));
                } else {
                    int subscriberCount = 0;
                    subscribers = subscriberRepo.findAllByProductIdEqualsAndStatusEquals(prod.getId(), 0);
                    log.info("=== Total contents found for product " + prod.getCode()
                            + "(" + prod.getId() + ")" + "  @" + (new Date()) + " == " + contents.size()
                            + " and total subscribers is == " + subscribers.size());
                    if (props.isChargePerSms()) {
                        for (SmsContent content : contents) {
                            for (Subscriber subscriber : subscribers) {
                                Provisioning prov = provisioningRepo
                                        .findOneByProductIdAndContentIdAndMsisdnAndDateLogged(prod.getId(),
                                                content.getId(), subscriber.getMsisdn(), date);
                                if (prov == null) {
                                    try {
                                        provisioning = new Provisioning();
                                        if (props.isNotifyChargeFailed()) {
                                            provisioning.setNotifyOnFailedBilling(true);
                                        }
                                        if (props.isPreChargeNotify()) {
                                            provisioning.setNotifyBeforeBilling(true);
                                        }
                                        provisioning.setBillPerSms(true);
                                        provisioning.setProductId(prod.getId());
                                        provisioning.setContentId(content.getId());
                                        provisioning.setMsisdn(subscriber.getMsisdn());
                                        provisioning.setStatus("fresh");
                                        provisioning.setTrialCount(0);
                                        provisioning.setDateLogged(date);
                                        provisioning.setNextRetrial(new Date());
                                        if (subscriber.getTrialCount() == null
                                                || subscriber.getTrialCount() == 0) {
                                            provisioning.setSuccessAverage(1.0);
                                        } else {
                                            int trialCount = subscriber.getTrialCount();
                                            int successCount;
                                            if (subscriber.getSuccessCount() == null) {
                                                successCount = 0;
                                            } else {
                                                successCount = subscriber.getSuccessCount();
                                            }
                                            double successAvg = (double) successCount / (double) trialCount;
                                            provisioning.setSuccessAverage(successAvg);
                                        }
                                        provisioningRepo.save(provisioning);
                                        subscriberCount = subscriberCount + 1;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log.info("SubscriptionService#prepareSubscription ==>  (1) ", e);
                                    }
                                }
                            }

                        }
                    } else {
                        SmsContent content = contents.get(0);
                        for (Subscriber subscriber : subscribers) {
                            // check if the subscriber is DnDed and skip
                            if (dndRepo.findById(subscriber.getMsisdn()).isPresent()) {
                                subscriber.setStatus(4);
                                subscriberRepo.save(subscriber);
                                continue;
                            }
                            Provisioning prov = provisioningRepo
                                    .findOneByProductIdAndContentIdAndMsisdnAndDateLogged(prod.getId(),
                                            content.getId(), subscriber.getMsisdn(), date);
                            if (prov == null) {
                                try {
                                    provisioning = new Provisioning();
                                    if (props.isNotifyChargeFailed()) {
                                        provisioning.setNotifyOnFailedBilling(true);
                                    }
                                    if (props.isPreChargeNotify()) {
                                        provisioning.setNotifyBeforeBilling(true);
                                    }
                                    provisioning.setBillPerSms(false);
                                    provisioning.setProductId(prod.getId());
                                    provisioning.setContentId(content.getId());
                                    provisioning.setMsisdn(subscriber.getMsisdn());
                                    provisioning.setStatus("fresh");
                                    provisioning.setTrialCount(0);
                                    provisioning.setDateLogged(date);
                                    provisioning.setNextRetrial(new Date());
                                    if (subscriber.getTrialCount() == null || subscriber.getTrialCount() == 0
                                            || subscriber.getSuccessCount() == null
                                            || subscriber.getSuccessCount() == 0) {
                                        provisioning.setSuccessAverage(1.0);
                                    } else {
                                        int successCount = subscriber.getSuccessCount();
                                        int trialCount = subscriber.getTrialCount();
                                        double successAvg = successCount / trialCount;
                                        provisioning.setSuccessAverage(successAvg);
                                    }
                                    provisioningRepo.save(provisioning);
                                    subscriberCount = subscriberCount + 1;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.info("SubscriptionService#prepareSubscription ==>  (2) ", e);
                                }
                            }
                        }
                    }
                    log.info("=== Total subscribers submitted for billing for product " + prod.getCode()
                            + " @  " + (new Date()) + " ==   " + subscriberCount);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SubscriptionService#prepareSubscription ==>  (3) ", e);
        }
        return 0;
    }

    public ProvisioningRepository getProvisioningRepo() {
        return provisioningRepo;
    }

    /**
     * Takes daily report and sends to the number.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void takeStats() {
        // intentionally not moved to autoSwitch()
        ZonedDateTime now = ZonedDateTime.now();
        if (now.getHour() == 1) {
            LocalDate ld = LocalDate.now();
            ld = ld.minusDays(1);
            Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            // Take statistics by 1 a.m
            long total;
            ProvisioningStat provStat;
            long untried = provisioningRepo.countByStatusEqualsAndDateLoggedIs("fresh", date);
            long failed = provisioningRepo.countByStatusEqualsAndDateLoggedIs("failed", date);
            long success = provisioningRepo.countByStatusEqualsAndDateLoggedIs("fulfilled", date);
            total = untried + failed + success;
            provStat = new ProvisioningStat("All", toIntExact(total),
                    toIntExact(failed), toIntExact(success), toIntExact(untried), date,
                    "category_based");
            provisioningStatRepo.save(provStat);

            List<Product> products = productSvc.findAll();
            for (Product prod : products) {
                long c1 = provisioningRepo.countByProductIdAndDateLogged(prod.getId(), date);
                long c2 = provisioningRepo.countByDateLoggedIsAndProductIdIsAndStatusContainingIgnoreCase(date,
                        prod.getId(), "failed");
                long c3 = provisioningRepo.countByDateLoggedIsAndProductIdIsAndStatusContainingIgnoreCase(date,
                        prod.getId(), "fulfilled");
                // record only products that were provisioned
                // c1 = total, c2 = failed, c3 = successful, c4 = untried
                if (c1 > 0) {
                    long c4 = c1 - (c2 + c3);
                    provStat = new ProvisioningStat(StringUtils.capitalize(prod.getCode()), toIntExact(c1),
                            toIntExact(c2), toIntExact(c3), toIntExact(c4), date, "product_based");
                    provisioningStatRepo.save(provStat);
                }
            }

        }
    }

}
