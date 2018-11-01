package com.ft.service;

import java.net.URI;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;

import telsoft.app.object.SOAPRequest;
import telsoft.app.object.SOAPRequestService;

@EnableScheduling
@Service
public class SoapClientService {

    private final Logger log = LoggerFactory.getLogger(SoapClientService.class);

    @Autowired
    private ApplicationProperties props;
    
    /**
     * Execute every 15 minutes, from 08AM - 21PM everyday
     */
//    @Scheduled(fixedDelay = 60000)
    public int submit() throws Exception {
//		SOAPRequest client = new SOAPRequestService(URI.create(props.getLuUrl()).toURL()).getSOAPRequestPort();
//		client.receiverServiceReq(isdn, serviceCode, commandCode, packageCode, sourceCode, user, password, description);
//		client.confirmMinusMoney(transactionId, otp, userName, password);
//		client.exeReceivedCPMT(serviceCode, packageCode, contents, userName, password);
//		client.getInfomationCcgw(serviceCode, isdn, sourceCode, userName, password);
//		client.minusMoneyOtp(serviceCode, isdn, contentId, contentName, amount, userName, password);
//		client.sendMessage(serviceCode, isdn, content, user, password);
//		client.receiverServiceReq(isdn, serviceCode, commandCode, packageCode, sourceCode, user, password, description);
    	return 0;
    }
}
