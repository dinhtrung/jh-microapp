package com.ft.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;
import com.ft.soap.Result;
import com.ft.soap.SOAPMCA;

@EnableScheduling
@Service
public class SoapServerService implements SOAPMCA {

    private final Logger log = LoggerFactory.getLogger(SoapServerService.class);

    @Autowired
    private ApplicationProperties props;

	@Override
	public Result sendLU(String tid, String imsi, String cdpa, String cgpa) {
		// TODO Auto-generated method stub
		log.debug("sendLU: tid=" + tid + ", imsi=" + imsi + ", cdpa=" + cdpa + ", cgpa=" + cgpa);
		Result result = new Result();
		result.setResponseCode(0);
		return result;
	}

	@Override
	public Result sendCL(String tid, String imsi, String cdpa, String cgpa) {
		log.debug("sendCL: tid=" + tid + ", imsi=" + imsi + ", cdpa=" + cdpa + ", cgpa=" + cgpa);
		Result result = new Result();
		result.setResponseCode(0);
		return result;
	}

	@Override
	public Result sendISD(String tid, String msisdn) {
		log.debug("sendISD: tid=" + tid + ", msisdn=" + msisdn);
		Result result = new Result();
		result.setResponseCode(0);
		return result;
	}
}
