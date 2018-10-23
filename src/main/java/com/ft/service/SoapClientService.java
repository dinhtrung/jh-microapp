package com.ft.service;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;
import com.ft.soap.Result;
import com.ft.soap.SOAPMCA_Service;

@EnableScheduling
@Service
public class SoapClientService {

    private final Logger log = LoggerFactory.getLogger(SoapClientService.class);

    @Autowired
    private Executor scheduledTaskExecutor;

    @Autowired
    private ApplicationProperties props;


    /**
     * Execute every 15 minutes, from 08AM - 21PM everyday
     */
    @Scheduled(fixedDelay = 60000)
    public int submit() throws Exception {
    	SOAPMCA_Service soapClient = new SOAPMCA_Service(new URL(props.getServerUrl()));
		String tid = RandomStringUtils.randomNumeric(10);
		String imsi = RandomStringUtils.randomNumeric(10);
		String cdpa = RandomStringUtils.randomNumeric(10);
		String cgpa = RandomStringUtils.randomNumeric(10);
		Result resp = soapClient.getSOAPMCA().sendLU(tid, imsi, cdpa, cgpa);
    	return 0;
    }
}
