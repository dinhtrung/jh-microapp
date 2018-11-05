package com.ft.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.ft.config.ApplicationProperties;
import com.ft.config.SmsMtConfig;
import com.ft.service.dto.SmsDTO;

@EnableScheduling
@EnableAsync
@Service
public class SendSmsService {

	private final Logger log = LoggerFactory.getLogger(SendSmsService.class);

	@Autowired
	private SmsMtConfig mtsender;

	@Autowired
	private ApplicationProperties props;


	public void submit(SmsDTO sms) {
		try {
			SubmitSmResp response = mtsender.sendMessage(sms.getSource(), sms.getText(), sms.getDestination());
			sms.setMessageId(response.getMessageId());
			if (response.getCommandStatus() == 0){
				log.debug("Successfully submit SMS: '" + sms.getText() + "'");
				sms.setStatus(SmsDTO.STATE_SUBMITTED);
			} else {
				log.debug("Submit SMS failed with code " + response.getCommandStatus());
				sms.setStatus(sms.getStatus() - 1);
			}
		} catch (Exception e) {
			log.error("Cannot send SMS: '" + sms + "'");
			e.printStackTrace();
		}
	}

	public void SubmitRespSmsList(List<SmsDTO> sms) {
		for (SmsDTO s:sms){
			submit(s);
		}
	}
}
