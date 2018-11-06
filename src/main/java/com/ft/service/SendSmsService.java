package com.ft.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.util.DeliveryReceipt;
import com.ft.component.SmsSubmitCallable;
import com.ft.config.ApplicationProperties;
import com.ft.config.SmsMtConfig;
import com.ft.domain.Sms;
import com.ft.domain.SmsLog;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SmsRepository;

@EnableScheduling
@EnableAsync
@Service
public class SendSmsService {

    private final Logger log = LoggerFactory.getLogger(SendSmsService.class);

    @Autowired
    private SmsLogRepository smsRepo;

    @Autowired
    private SmsRepository respRepo;

    @Autowired
    private SmsMtConfig mtsender;

    @Autowired
    private Executor smsSendingExecutor;

    @Autowired
    private ApplicationProperties props;

    public void submit(SmsLog sms) {
        try {
            SubmitSmResp response = mtsender.sendMessage(sms.getSource(), sms.getText(), sms.getDestination());
            sms.setMessageId(response.getMessageId());
            if (response.getCommandStatus() == 0) {
                //log.debug("Successfully submit SMS: '" + sms.getText() + "'");
                sms.setStatus(SmsLog.STATE_SUBMITTED);
            } else {
                //log.debug("Submit SMS failed with code " + response.getCommandStatus());
                sms.setStatus(sms.getStatus() - 1);
                sms.setTag(response.getResultMessage());
            }
        } catch (Exception e) {
            log.error("Cannot send SMS: '" + sms + "'");
            e.printStackTrace();
        }
        try {
            //log.debug("Got SMS: " + sms);
            smsRepo.save(sms);
        } catch (Exception e) {
            log.error("Cannot update SMS status " + sms);
            smsRepo.delete(sms);
            e.printStackTrace();
        }
    }

    public void submit(Sms sms) {
        try {
            SubmitSmResp response = mtsender.sendMessage(sms.getSource(), sms.getText(), sms.getDestination());
            sms.setMessageId(response.getMessageId());
            if (response.getCommandStatus() == 0) {
                //log.debug("Successfully submit SMS: '" + sms.getText() + "'");
                sms.setStatus(Sms.STATE_SUBMITTED);
            } else {
                //log.debug("Submit SMS failed with code " + response.getCommandStatus());
                sms.setStatus(sms.getStatus() - 1);
            }
            respRepo.save(sms);
        } catch (Exception e) {
            log.error("Cannot send SMS: '" + sms + "'");
            e.printStackTrace();
        }
    }

    public void SubmitList(List<SmsLog> sms) {
//		smsSendingExecutor.(new SmsSubmitCallable(sms, mtsender, smsRepo));
    }

    public void SubmitRespSmsList(List<Sms> sms) {
        for (Sms s : sms) {
            submit(s);
        }
    }

    /**
     * Execute every 15 minutes, from 08AM - 21PM everyday
     */
    @Scheduled(fixedDelay = 10000)
    public int submit() throws InterruptedException, ExecutionException {
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(smsSendingExecutor);
        int threads = 0;
        List<SmsLog> smsList = smsRepo.findAllByStatusBetweenAndSubmitAtLessThan(SmsLog.STATE_FAILED, SmsLog.STATE_SUBMITTED, ZonedDateTime.now());
        if (smsList.size() == 0) {
            return 0;
        }
        for (List<SmsLog> submitList : ListUtils.partition(smsList, props.getBatchSize())) {
            completionService.submit(new SmsSubmitCallable(submitList, mtsender, smsRepo));
            threads++;
        }
        //log.info("=== Submit all charge requests into " + threads + " threads");
        Future<Integer> completedFuture;
        int successCharged = 0;
        while (threads > 0) {
            // block until a callable completes
            completedFuture = completionService.take();
            threads--;

            // get the Widget, if the Callable was able to create it
            try {
                Integer res = completedFuture.get();
                if (completedFuture.isDone()) {
                    successCharged += res;
                    //log.info("=== ONE THREAD COMPLETED: " + threads + " REQUEST SEND: " + res);
                } else {
                    //log.error("=== WHY THREAD IS NOT DONE HERE ===" );
                }
            } catch (ExecutionException e) {
                log.error("== CANNOT RUN RENEW TASK", e);
                continue;
            }
        }
        //log.info("=== Total success processed: " + successCharged);
        return successCharged;
    }

    /**
     * Deliver SMS Response to customer every 10 seconds
     */
    @Scheduled(fixedDelay = 10000)
    public void sendSmsResponse() {
        SubmitRespSmsList(respRepo.findAllByStatusBetweenAndSubmitAtLessThan(Sms.STATE_FAILED, Sms.STATE_SUBMITTED, ZonedDateTime.now()));
    }

    public void saveDlr(DeliveryReceipt dlr) {
        SmsLog sms = smsRepo.findOneByMessageId(dlr.getMessageId());
        if (sms != null) {
            smsRepo.save(sms.status(dlr.getState() == SmppConstants.STATE_DELIVERED ? SmsLog.STATE_DELIVERED : SmsLog.STATE_FAILED));
        }
        Sms req = respRepo.findOneByMessageId(dlr.getMessageId());
        if (req != null) {
            respRepo.save(req.status(dlr.getState() == SmppConstants.STATE_DELIVERED ? Sms.STATE_DELIVERED : Sms.STATE_FAILED));
        }
    }
}
