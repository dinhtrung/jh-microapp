package com.ft.component;

import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.ft.config.SmsMtConfig;
import com.ft.domain.SmsLog;
import com.ft.repository.SmsLogRepository;

public class SmsSubmitCallable implements Callable<Integer> {

    private final Logger log = LoggerFactory.getLogger(SmsSubmitCallable.class);

    private final List<SmsLog> smsList;
    private final SmsMtConfig mtsender;
    private final SmsLogRepository smsRepo;

    public SmsSubmitCallable(List<SmsLog> smsList, SmsMtConfig mtsender, SmsLogRepository smsRepo) {
        super();
        this.smsList = smsList;
        this.mtsender = mtsender;
        this.smsRepo = smsRepo;
        //log.info("== THREAD " + this.hashCode() + ": Submit " + smsList.size() + " SMS");
    }

    public void submit(SmsLog sms) throws Exception {
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
        smsRepo.save(sms);
    }

    @Override
    public Integer call() throws Exception {
        int i = 0;
        for (SmsLog s : smsList) {
            try {
                submit(s);
                i++;
            } catch (Exception e) {
                log.error("Cannot send SMS: '" + s + "'", e);
            }
        }
        return i;
    }
}
