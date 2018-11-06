package com.ft.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.annotation.Timed;
import com.ft.config.ApplicationProperties;
import com.ft.domain.Cdr;
import com.ft.domain.Product;
import com.ft.domain.Provisioning;
import com.ft.domain.Sms;
import com.ft.domain.SmsContent;
import com.ft.domain.SmsLog;
import com.ft.domain.Subscriber;
import com.ft.domain.Subscription;
import com.ft.repository.CdrRepository;
import com.ft.repository.DndRepository;
import com.ft.repository.ProvisioningRepository;
import com.ft.repository.SmsContentRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SmsRepository;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;
import com.ft.repository.SubscriptionRepository;
import com.ft.service.SendSmsService;
import com.ft.service.SmsContentService;
import com.ft.service.SubMsisdnService;
import com.ft.service.SubProductService;
import com.ft.service.SubscriptionService;
import com.ft.service.dto.ChargingProfile;
import com.ft.service.util.CsvUtil;
import com.ft.service.util.SmsSender;
import com.ft.web.api.model.ChargeRequest;
import com.ft.web.api.model.ChargeResponse;

/**
 * REST controller for managing Cdr.
 */
@RestController
@RequestMapping("/web-service")
public class WebServiceResource {

    @Autowired
    SendSmsService sms;

    @Autowired
    SubscriptionService subscription;

    @Autowired
    SubProductRepository productRepo;

    @Autowired
    private SubMsisdnService subMsisdnService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SubscriberRepository msisdnRepo;

    @Autowired
    private DndRepository dndRepo;

    @Autowired
    private SubProductRepository prodRepo;

    @Autowired
    private SmsLogRepository smsLogRepo;

    @Autowired
    private SmsContentRepository msgRepo;

    @Autowired
    private CdrRepository cdrRepo;

    @Autowired
    private ApplicationProperties props;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    CsvUtil csvUtil;

    @Autowired
    SmsSender smsSender;
    @Autowired
    SubscriptionRepository subscriptionRepo;

    @Autowired
    private SubscriberRepository subscriberRepo;

    @Autowired
    private SmsContentService msgSvc;

    @Autowired
    private SubProductService productSvc;

    @Autowired
    private SmsRepository smsRepo;

    @Autowired
    private ProvisioningRepository provisioningRepo;

    /**
     * Subscription endpoint
     * /airtel/web-service/api&channel=MAMO&notify=1&ani=2348124795001&dnis=38661&package=SUCCESS
     *
     * @param msisdn
     * @param notify
     * @param keyword
     * @param subType
     * @param shortCode
     * @param partner
     * @param channel
     * @return
     */
    @GetMapping("/subscription")
    @Timed
    public ResponseEntity<String> actionApi(
            @RequestParam("msisdn") String msisdn,
            @RequestParam("package") String keyword,
            @RequestParam("sc") String shortCode,
            @RequestParam("channel") String channel,
            @RequestParam("cp") String partner,
            @RequestParam("type") String subType,
            @RequestParam("notify") Integer notify) {
        return handleSubscription(keyword, msisdn, channel, shortCode, partner, notify);
        //return subscriptionService.actionApi(msisdn, productName, shortCode, channel, partner, subType, notify);
    }

    /**
     * Subscription endpoint
     * /airtel/web-service/api&channel=MAMO&notify=1&ani=2348124795001&dnis=38661&package=SUCCESS
     *
     * @param route
     * @param partner
     * @param channel
     * @param keyword
     * @param notify
     * @param shortCode
     * @param msisdn
     * @return
     */
    @GetMapping("/index.php")
    @Timed
    public ResponseEntity<String> mamoApi(
            @RequestParam("r") String route,
            @RequestParam(name = "channel", required = false, defaultValue = "SMS") String channel,
            @RequestParam(name = "notify", required = false, defaultValue = "0") Integer notify,
            @RequestParam("ani") String msisdn,
            @RequestParam("dnis") String shortCode,
            @RequestParam("package") String keyword,
            @RequestParam(name = "cp", required = false) String partner
    ) {
        return handleSubscription(keyword, msisdn, channel, shortCode, partner, notify);
        //return subscriptionService.actionApi(msisdn, keyword, shortCode, channel, partner, "SUB", notify);
    }

    /**
     * Subscription endpoint
     *
     * @param msisdn
     * @param notify
     * @param keyword
     * @param subType
     * @param shortCode
     * @param partner
     * @param channel
     * @return
     */
    @GetMapping("/subscription-api")
    @Timed
    public ResponseEntity<String> subscriptionApi(
            @RequestParam("msisdn") String msisdn,
            @RequestParam("pkg") String keyword,
            @RequestParam("sc") String shortCode,
            @RequestParam("channel") String channel,
            @RequestParam("cp") String partner,
            @RequestParam("type") String subType,
            @RequestParam("notify") Integer notify) {
        if (dndRepo.findById(msisdn).isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are in DND list");
        }
        Product product = prodRepo.findOneByJoinPatternIgnoreCase(keyword);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        // Here comes the flow...
        Subscriber sub = msisdnRepo.findOneByProductIdAndMsisdn(product.getId(), msisdn);
        if (sub == null) {
            sub = new Subscriber();
            sub.setMsisdn(msisdn);
            sub.setproductId(product.getId());
        }
        SmsLog smsLog = props.getSms();
        if (subType.equalsIgnoreCase("SUB")) {
            return handleSubscription(keyword, msisdn, channel, shortCode, partner, notify);
//            sub.setJoinAt(ZonedDateTime.now());
//            sub.setJoinChannel(channel);
//            sub.setPartnerCode(partner);
//            sub.setStatus(0);
//            sms.text(product.getMsgWelcome());
//            // Load SMS for today
//            List<SmsContent> msgList = msgRepo.findAllByProductIdAndStartAtGreaterThanAndExpiredAtLessThan(product.getId(), ZonedDateTime.now(), ZonedDateTime.now());
//            for (SmsContent msg : msgList) {
//                SmsLog m = new SmsLog();
//                m.text(msg.getMessage())
//                        .destination(msisdn)
//                        .source(product.getBroadcastShortcode())
//                        .submitAt(msg.getStartAt())
//                        .status(0)
//                        .tag(msg.getId());
//
//                smsRepo.save(m);
//            }
            //sub.setStatus(1);
        } else if (subType.equalsIgnoreCase("UNSUB")) {
            if (sub.getStatus() == null) {
                smsLog.text(product.getMsgNotUsed());
            } else if (sub.getStatus() == 2) {
                smsLog.text(product.getMsgAlreadyLeft());
            } else {
                smsLog.text(product.getMsgFarewell());
                sub.setLeftAt(ZonedDateTime.now());
                sub.setStatus(2);
                sub.setLeftChannel(channel);
                // FIXME: Delete all pending SMS
                smsLogRepo.deleteByDestinationAndSubmitAtGreaterThan(msisdn, ZonedDateTime.now());
            }
        }
        // Only skip notify if `notify=0`
        if (notify != 0) {
            smsLogRepo.save(smsLog);
        }
        msisdnRepo.save(sub);
        return ResponseEntity.accepted().body("OK");
    }

    /**
     * SMS
     *
     * @param msisdn
     * @param sc
     * @param text
     * @return
     */
    @GetMapping("/sms")
    @Timed
    public ResponseEntity<String> actionSms(@RequestParam("msisdn") String msisdn,
            @RequestParam("sc") String sc, @RequestParam("text") String text) {
        SmsLog smsLog = new SmsLog(props.getSms());
        if (sc != null) {
            smsLog.source(sc);
        }
        smsLog.destination(msisdn).text(text).submitAt(ZonedDateTime.now()).status(0);
        smsLogRepo.save(smsLog);
        return ResponseEntity.accepted().body("OK");
    }

    /**
     * GET /sub-msisdn/:id : get the "id" dnd.
     *
     * @return
     */
    @GetMapping("/reset-subscriber")
    @Timed
    public ResponseEntity<Long> resetMsisdn() {
        return ResponseEntity.ok().body(subscriptionService.resetSubscribers());
    }

    /**
     * GET /sub-msisdn/:id : get the "id" dnd.
     *
     * @param msisdn
     * @param productId
     * @return
     */
    @GetMapping("/subscriber")
    @Timed
    public ResponseEntity<Subscriber> getMsisdn(@RequestParam("m") String msisdn, @RequestParam("p") String productId) {
        Subscriber sub = msisdnRepo.findOneByMsisdnAndProductId(msisdn, productId);
        if (sub == null) {
            Product product = prodRepo.findOneByJoinPatternIgnoreCase(productId);
            if (product != null) {
                sub = msisdnRepo.findOneByMsisdnAndProductId(msisdn, product.getId());
            }
        }
        if (sub == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(sub);
    }

    /**
     * GET /sub-msisdn/:id : get the "id" dnd.
     *
     * @param msisdn
     * @return
     */
    @GetMapping("/subscriber-lookup")
    @Timed
    public ResponseEntity<List<Subscriber>> lookupMsisdn(@RequestParam("m") String msisdn) {
        List<Subscriber> sub = msisdnRepo.findAllByMsisdn(msisdn);
        return ResponseEntity.ok().body(sub);
    }

    @GetMapping("/subscription/stats/{todayOnly}")
    @Timed
    public String subscriptionStats(boolean todayOnly) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime yest = now.minusDays(1);
        LocalDate ld1 = yest.toLocalDate();
        LocalDate ld = LocalDate.now();
        Instant instant1 = ld1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        StringBuilder builder = new StringBuilder();

        if (!todayOnly) {
            Date yesterday = Date.from(instant1);
            long yesterdayAll = subscriptionService.getProvisioningRepo()
                    .countByDateLoggedIs(yesterday);
            long yesterdayFresh = subscriptionService.getProvisioningRepo()
                    .countByStatusEqualsAndDateLoggedIs("fresh", yesterday);
            long yesterdayFailed = subscriptionService.getProvisioningRepo()
                    .countByStatusEqualsAndDateLoggedIs("failed", yesterday);
            long yesterdayFulfilled = subscriptionService.getProvisioningRepo()
                    .countByStatusEqualsAndDateLoggedIs("fulfilled", yesterday);

            builder.append("<h4>");
            builder.append("Yesterday");
            builder.append("</h4>");
            builder.append("<div style='padding: 10px;'>");
            builder.append("Total provisioned:      ");
            builder.append(yesterdayAll);
            builder.append("<br/>");
            builder.append("Successful:             ");
            builder.append(yesterdayFulfilled);
            builder.append("<br/>");
            builder.append("Failed:                 ");
            builder.append(yesterdayFailed);
            builder.append("<br/>");
            builder.append("Un-tried:               ");
            builder.append(yesterdayFresh);
            builder.append("</div>");
            builder.append("<hr/>");
        }

        Date today = Date.from(instant);
        long todayAll = subscriptionService.getProvisioningRepo()
                .countByDateLoggedIs(today);
        long todayFresh = subscriptionService.getProvisioningRepo()
                .countByStatusEqualsAndDateLoggedIs("fresh", today);
        long todayFailed = subscriptionService.getProvisioningRepo()
                .countByStatusEqualsAndDateLoggedIs("failed", today);
        long todayFulfilled = subscriptionService.getProvisioningRepo()
                .countByStatusEqualsAndDateLoggedIs("fulfilled", today);

        builder.append("<h4>");
        builder.append("Today");
        builder.append("</h4>");
        builder.append("<div style='padding: 10px;'>");
        builder.append("Total provisioned:      ");
        builder.append(todayAll);
        builder.append("<br/>");
        builder.append("Successful:             ");
        builder.append(todayFulfilled);
        builder.append("<br/>");
        builder.append("Failed:                 ");
        builder.append(todayFailed);
        builder.append("<br/>");
        builder.append("Un-tried:               ");
        builder.append(todayFresh);
        builder.append("</div>");
        builder.append("<br/><br/>");

        return builder.toString();
    }

    @GetMapping("/sub/report/{status}")
    @Timed
    public String shortReport(@PathVariable String status) {
        LocalDate ld = LocalDate.now();
        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date today = Date.from(instant);
        long count = subscriptionService.getProvisioningRepo()
                .countByStatusEqualsAndDateLoggedIs(status, today);
        return "Total " + status + ":  " + count;
    }

    @RequestMapping(value = "/sub/ncc/data/download")
    @ResponseBody
    public void downloadNccData(@RequestParam("startIndex") Integer startIndex,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("download") Boolean download,
            HttpSession session, HttpServletResponse response) {
        try {
            csvUtil.createNccCsv(startIndex, pageSize);
            if (download) {
                String filePathToBeServed = "ncc_requested_data_" + startIndex + "_"
                        + (startIndex + pageSize) + ".csv";
                File fileToDownload = new File(filePathToBeServed);
                try (InputStream inputStream = new FileInputStream(fileToDownload)) {
                    response.setContentType("application/force-download");
                    response.setHeader("Content-Disposition", "attachment; filename=" + filePathToBeServed);
                    IOUtils.copy(inputStream, response.getOutputStream());
                    response.flushBuffer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("downloadNccData  -->", e);
        }
    }

    @GetMapping("/subscription/rerun")
    @Timed
    public String rerunProvisioning() {
        try {
            long start = System.currentTimeMillis();
            subscriptionService.provisionSubscribers();
            long end = System.currentTimeMillis();
            long diff = end - start;
            double duration = diff / 1000;
            return "Total time: " + duration + "sec";
        } catch (InterruptedException | ExecutionException ex) {
            java.util.logging.Logger.getLogger(WebServiceResource.class.getName()).log(Level.SEVERE, null, ex);
            return "ex";
        }
    }

    @GetMapping("/recover/{msisdn}/{pid}")
    @Timed
    public String recoverSubscriber(@PathVariable String msisdn,
            @PathVariable String pid) {
        Subscriber subscriber = subMsisdnService.findOneByMsisdnAndProduct(msisdn, pid);
        if (subscriber == null) {
            subscriber = new Subscriber();
            subscriber.setproductId(pid);
            subscriber.setMsisdn(msisdn);
            subscriber.setStatus(Subscriber.STATUS_PROCESSED);
            subscriber.setJoinAt(ZonedDateTime.now());
            subscriber.setJoinChannel("DATA-FILE");
            subscriber.setChargeLastSuccess(ZonedDateTime.now());
            subscriber.setChargeLastTime(ZonedDateTime.now());
            subscriber.setChargeNextTime(ZonedDateTime.now());
            subscriber.setExpiryTime(ZonedDateTime.now());
            subMsisdnService.save(subscriber);
            return "ok";
        }
        return "exists";
    }

    @Autowired
    RestTemplate restTemplate;

    // {"tranxId":"782376345743", "code":200, "description":" The payment was successful", "contextMsg":""}
    @PostMapping("/charge-old")
    @Timed
    public ResponseEntity<ChargeResponse> chargeResult(@RequestBody ChargeResponse chargeResult) {
        log.info("GOT REQUEST: " + chargeResult);
//      String msisdn = props.msisdnFormat(chargeResult.getMsisdn());
        Optional<Cdr> cdrOpt = cdrRepo.findById(chargeResult.getExtTxnId());
        if (!cdrOpt.isPresent()) {
            return ResponseEntity.accepted().body(chargeResult); // Shold be NOt found but now we want this
        }
        Cdr cdr = cdrOpt.get();
        Optional<Product> prodOpt = prodRepo.findById(cdr.getProductId());
        if (!prodOpt.isPresent()) return ResponseEntity.accepted().body(chargeResult); // Shold be NOt found but now we want this
        Product prod = prodOpt.get();
        // Find the product by ChargingProfile
        Subscriber subscriber = msisdnRepo.findOneByMsisdnAndProductId(cdr.getMsisdn(), prod.getId());
        ChargingProfile profile = prod.getChargingProfiles().get(0);
        for (int i = 1; i < prod.getChargingProfiles().size(); i++) {
            if (prod.getChargingProfiles().get(i).getChargePrice() == cdr.getAmount().intValue()) {
                profile = prod.getChargingProfiles().get(i);
            }
        }
        if (chargeResult.getCode() == 200) {
            msisdnRepo.save(
                    subscriber
                    .status(Subscriber.STATUS_PROCESSED)
                    .notify(Subscriber.NOTIFY_PENDING)
                    .chargeLastSuccess(ZonedDateTime.now())
                    .chargeNextTime(ZonedDateTime.now().plusDays(profile.getChargePeriod()).withHour(1))
                    .expiryTime(ZonedDateTime.now().plusDays(profile.getChargePeriod()))
            );
            cdr = cdrRepo.save(cdr.status(true).responsePayload(chargeResult.toString()));
        } else {
            cdr = cdrRepo.save(cdr.status(false).responsePayload(chargeResult.toString())
            );
        }
        return ResponseEntity.ok().body(chargeResult);
    }

    @GetMapping("/charge")
    public ResponseEntity<String> doCharge(
            @RequestParam("m") String msisdn,
            @RequestParam("p") String pkg,
            @RequestParam("a") String amount,
            @RequestParam(name = "i", required = false) String id) {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        ChargeRequest request = new ChargeRequest();
                request.setAmount(amount);
                request.setId(id);
                request.setMsisdn(msisdn);
                request.setServiceName(pkg);
        return restTemplate.postForEntity(props.getChargingUrl(), request, String.class);
    }

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public void EtisalatAsyncNotification(@RequestBody Map<String, Object> payload) {
        if (payload == null) {
            log.info("Payload is NULL");
            return;
        }
        try {
            log.info("=========================Async Response=============================");
            log.info(payload.toString());
            log.info("========================</End Async Response=========================");
        } catch (Exception e) {
        }
        Object tranxId = payload.get("extTxnId");
        if (tranxId == null) {
            log.info("Tranx ID is NULL");
            return;
        }
        String id = tranxId.toString();
        Optional<Provisioning> provOpt = provisioningRepo.findById(id);
        if (!provOpt.isPresent()) {
            log.info("Provisioning is NULL. Here is the tranxId -->  " + tranxId);
            return;
        }
        Provisioning prov = provOpt.get();
        if (payload.get("code") == null) {
            log.info("Code is NULL");
            return;
        }
        Cdr transaction = new Cdr()
                .productId(prov.getProductId())
                .contentId(prov.getContentId())
                .msisdn(prov.getMsisdn())
                .note(prov.getId())
                .requestAt(ZonedDateTime.now())
                .responseAt(ZonedDateTime.now())
                .requestPayload(payload.toString());
        String code = payload.get("code").toString();
        switch (code) {
            case "200":
                prov.setStatus("fulfilled");
                Subscriber subscriber = subscriberRepo.findOneByMsisdn(prov.getMsisdn());
                Optional<Product> productOpt = productSvc.findOne(prov.getProductId());
                if (productOpt.isPresent()) {
                	Product product = productOpt.get();
                	Sms response = new Sms(props.getSms())
                            .destination(subscriber.getMsisdn())
                            .status(SmsLog.STATE_PENDING)
                            .submitAt(ZonedDateTime.now());
                    response.text(product.getMsgWelcome());
                    response.productId(product.getId());
                    smsRepo.save(response);
                    List<SmsContent> contents = msgSvc.getTodayContents(product);
                    try {
                        for (Iterator<SmsContent> it = contents.iterator(); it.hasNext();) {
                            SmsContent smsContent = it.next();
//                            Sms response = new Sms(props.getSms())
//                                    .destination(subscriber.getMsisdn())
//                                    .status(SmsLog.STATE_PENDING)
//                                    .submitAt(ZonedDateTime.now());
//                            response.text(smsContent.getMessage());
//                            response.productId(product.getId());
//                            smsRepo.save(response);
                            smsLogRepo.save(
                                    new SmsLog()
                                    .source(product.getBroadcastShortcode())
                                    .destination(subscriber.getMsisdn())
                                    .text(smsContent.getMessage())
                                    .contentId(smsContent.getId())
                                    .productId(product.getId())
                                    .submitAt(smsContent.getStartAt())
                                    .status(SmsLog.STATE_PENDING)
                            );
                        }
                    } catch (Exception e) {
                    }
                    transaction.status(true).amount(10.0);
                    // update subscriber
                    subscriber.setSuccessCount(subscriber.getSuccessCount() != null ? subscriber.getSuccessCount() + 1 : 1);
                    subscriber.setChargeLastSuccess(ZonedDateTime.now());
                    subscriberRepo.save(subscriber);
                }
                break;
            case "232":
                // 232 == processed before
                prov.setStatus("failed_" + code);
                break;
            case "236":
                // 236 == service should be synchronous
                prov.setStatus("failed_" + code);
                break;
            case "237":
                // 237 == billed in the last 24 hrs
                prov.setStatus("failed_" + code);
                break;
            case "238":
                // 238 == already been processed
                prov.setStatus("failed_" + code);
                break;
            default:
                //String[] failureCodes = {"222", "225", "226", "227", "228", "229", "230", "231"};
                prov.setStatus("failed");
                transaction.status(false).amount(10.0);
        }
        prov.setTrialCount(prov.getTrialCount() + 1);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, prov.getTrialCount() * 10);
        prov.setNextRetrial(instance.getTime());
        provisioningRepo.save(prov);
        cdrRepo.save(transaction);
        log.info("Async response completed successfully!");
    }

    private void saveSubscription(Product prod, String keyword, Subscriber subscriber,
            String channel, String shortCode, String errorCode, String errorMsg, String status) {
        Subscription sub = new Subscription();
        sub.setProductId(prod.getId());
        sub.setProductDesc(prod.getCode());
        sub.setStatus(status);
        sub.setSubKeyword(keyword);
        sub.setSubscriberId(subscriber.getId());
        sub.setSubscriptionChannel(channel);
        sub.setSubscriptionDate(new Date());
        sub.setPartnerCode(shortCode);
        sub.setSubErrorCode(errorCode);
        sub.setSubErrorMessage(errorMsg);
        subscriptionRepo.save(sub);
    }

    private ResponseEntity<String> handleSubscription(String keyword, String msisdn, String channel, String shortCode, String partner, Integer notify) {
        keyword = keyword.trim().toLowerCase();
        Subscriber subscriber;
        msisdn = msisdn.replaceAll("[^0-9]", "");
        List<Subscriber> subscribers = subscriberRepo.findAllByMsisdn(msisdn);
        if (subscribers != null && !subscribers.isEmpty()) {
            subscriber = subscribers.get(0);
        } else {
            subscriber = new Subscriber();
            subscriber.setMsisdn(msisdn);
            subscriber.setSuccessCount(0);
            subscriber.setTrialCount(0);
            subscriber.setRegDate(new Date());
            subscriber.setRegChannel(channel);
            subscriber.setStatus(0);
            subscriberRepo.save(subscriber);
        }
        List<Subscription> subs = subscriptionRepo.findAllBySubscriberId(subscriber.getId());
        if (subs != null && !subs.isEmpty()) {
            for (Subscription sub : subs) {
                if (sub.getStatus().equalsIgnoreCase("PENDING")) {
                    if (keyword != null && keyword.equals("yes")) {
                        try {
                            sub.setStatus("ACTIVE");
                            sub.setActivationDate(new Date());
                            subscriptionRepo.save(sub);
                            return subscriptionService.actionApi(msisdn, keyword,
                                    shortCode, channel, partner, "SUB", notify);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Optional<Product> prodOpt = prodRepo.findById(sub.getProductId());

                            Product prod = prodOpt.get();
                            String doubleOptinMsg = "Welcome on board! You're only a step away. Please reply with YES to activate the service";
                            this.smsSender.sendMessage(shortCode, msisdn, doubleOptinMsg + " " + prod.getCode(), "501");
                            return ResponseEntity.accepted().body("OK");
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        Product prod = null;
        List<Product> products = prodRepo.findAll();
        for (Product product : products) {
            List<String> keys = product.getJoinPattern();
            for (String key : keys) {
                if (key.trim().equalsIgnoreCase(keyword)) {
                    prod = product;
                }
            }
        }
        try {
            if (prod == null) {
                String text = "To subscribe for Time Management text TIME, for Human Relationship Management text NL, for Body Language text BODY to 33085";
                this.smsSender.sendMessage(shortCode, msisdn, text, "501");
            } else {
                saveSubscription(prod, keyword, subscriber, channel, prod.getBroadcastShortcode(), "nill", "nil", "PENDING");
                String doubleOptinMsg = "Welcome on board! You're only a step away. Please reply with YES to activate the service";
                this.smsSender.sendMessage(shortCode, msisdn, doubleOptinMsg + " " + prod.getCode(), "501");
            }
        } catch (Exception e) {
        }
        return ResponseEntity.accepted().body("OK");
    }

}
