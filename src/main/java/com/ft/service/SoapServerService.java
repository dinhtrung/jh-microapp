package com.ft.service;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;

import tns.ns.Result;
import tns.ns.SOAPMCA;

@EnableScheduling
@Service
public class SoapServerService implements SOAPMCA {

    private final Logger log = LoggerFactory.getLogger(SoapServerService.class);

    @Autowired
    private ApplicationProperties props;

	@Override
	public Result sendLU(String tid, String imsi, String cdpa, String cgpa) {
		// TODO Auto-generated method stub
		Result result = new Result();
		result.setResponseCode(RandomUtils.nextInt());
		log.debug("sendLU: tid=" + tid + ", imsi=" + imsi + ", cdpa=" + cdpa + ", cgpa=" + cgpa + " >> RESULT: " + result.getResponseCode());
		return result;
	}

	@Override
	public Result sendCL(String tid, String imsi, String cdpa, String cgpa) {
		Result result = new Result();
		result.setResponseCode(RandomUtils.nextInt());
		log.debug("sendCL: tid=" + tid + ", imsi=" + imsi + ", cdpa=" + cdpa + ", cgpa=" + cgpa + " >> RESULT: " + result.getResponseCode());
		return result;
	}

	@Override
	public Result sendISD(String tid, String msisdn) {
		Result result = new Result();
		result.setResponseCode(RandomUtils.nextInt());
		log.debug("sendISD: tid=" + tid + ", msisdn=" + msisdn + " >> RESULT: " + result.getResponseCode());
		return result;
	}
}
