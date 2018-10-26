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

import tns.ns.Result;
import tns.ns.SOAPMCA_Service;

@EnableScheduling
@Service
public class SoapClientService {

    private final Logger log = LoggerFactory.getLogger(SoapClientService.class);

    @Autowired
    private ApplicationProperties props;
    
    /**
     * Execute every 15 minutes, from 08AM - 21PM everyday
     */
    @Scheduled(fixedDelay = 60000)
    public int submit() throws Exception {
		String tid = RandomStringUtils.randomNumeric(10);
		String imsi = RandomStringUtils.randomNumeric(10);
		String msisdn = RandomStringUtils.randomNumeric(10);
		String cdpa = RandomStringUtils.randomNumeric(10);
		String cgpa = RandomStringUtils.randomNumeric(10);
		log.debug("tid=" + tid + ", imsi=" + imsi + ", msisdn=" + msisdn + ", cdpa=" + cdpa +  ", cgpa=" + cgpa);
		Result luResp = new SOAPMCA_Service(URI.create(props.getLuUrl()).toURL())
				.getSOAPMCA()
				.sendLU(tid, imsi, cdpa, cgpa);
		Result isdResp = new SOAPMCA_Service(URI.create(props.getIsdUrl()).toURL())
				.getSOAPMCA().sendISD(tid, msisdn);
		Result clResp = new SOAPMCA_Service(URI.create(props.getClUrl()).toURL())
				.getSOAPMCA().sendCL(tid, imsi, cdpa, cgpa);
		log.debug("LU=" + luResp.getResponseCode() + ", CL=" + clResp.getResponseCode() + ", ISD=" + isdResp.getResponseCode());
    	return 0;
    }
}
