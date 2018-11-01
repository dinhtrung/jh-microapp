package com.ft.service;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;

import telsoft.app.object.Exception_Exception;
import telsoft.app.object.SOAPRequest;

@EnableScheduling
@Service
public class SoapServerService implements SOAPRequest {

    private final Logger log = LoggerFactory.getLogger(SoapServerService.class);

    @Autowired
    private ApplicationProperties props;

	@Override
	public String minusMoney(String serviceCode, String isdn, String packageCode, String userName, String password)
			throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String minusMoneyOtp(String serviceCode, String isdn, String contentId, String contentName, String amount,
			String userName, String password) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendMessage(String serviceCode, String isdn, String content, String user, String password)
			throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receiverServiceReq(String isdn, String serviceCode, String commandCode, String packageCode,
			String sourceCode, String user, String password, String description) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exeReceivedCPMT(String serviceCode, String packageCode, String contents, String userName,
			String password) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String receiverPackageReq(String isdn, String serviceCode, String startDatetime, String endDatetime,
			String groupCode, String commandCode, String packageCode, String sourceCode, String user, String password,
			String description) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String confirmMinusMoney(String transactionId, String otp, String userName, String password)
			throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfomationCcgw(String serviceCode, String isdn, String sourceCode, String userName,
			String password) throws Exception_Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
