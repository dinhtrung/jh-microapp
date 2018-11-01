package com.ft.web.rest;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ft.config.ApplicationProperties;

import telsoft.app.object.SOAPRequest;
import telsoft.app.object.SOAPRequestService;

/**
 * REST controller for managing Cdr.
 */
@RestController
@RequestMapping("/api/soap")
public class SoapClientResource {

	@Autowired
	ApplicationProperties props;

	@Autowired
	SOAPRequest vasgateClient;
	
	@GetMapping("minusMoney")
	@Timed
	public ResponseEntity<String> minusMoney(@RequestParam String serviceCode, @RequestParam String isdn,
			@RequestParam String packageCode, @RequestParam String userName, @RequestParam String password)
			throws Exception {
		return ResponseEntity.ok().body(vasgateClient.minusMoney(serviceCode, isdn, packageCode, userName, password));
	}

	@GetMapping("minusMoneyOtp")
	@Timed
	public ResponseEntity<String> minusMoneyOtp(@RequestParam String serviceCode, @RequestParam String isdn,
			@RequestParam String contentId, @RequestParam String contentName, @RequestParam String amount,
			@RequestParam String userName, @RequestParam String password) throws Exception {
		return ResponseEntity.ok()
				.body(vasgateClient.minusMoneyOtp(serviceCode, isdn, contentId, contentName, amount, userName, password));
	}

	@GetMapping("sendMessage")
	@Timed
	public ResponseEntity<String> sendMessage(@RequestParam String serviceCode, @RequestParam String isdn,
			@RequestParam String content, @RequestParam String user, @RequestParam String password) throws Exception {
		return ResponseEntity.ok().body(vasgateClient.sendMessage(serviceCode, isdn, content, user, password));
	}

	@GetMapping("receiverServiceReq")
	@Timed
	public ResponseEntity<String> receiverServiceReq(
			@RequestParam String isdn, 
			@RequestParam String serviceCode,
			@RequestParam String commandCode, 
			@RequestParam String packageCode, 
			@RequestParam String sourceCode,
			@RequestParam String user, 
			@RequestParam String password, 
			@RequestParam String description
		)
			throws Exception {
		return ResponseEntity.ok().body(vasgateClient.receiverServiceReq(isdn, serviceCode, commandCode, packageCode,
				sourceCode, user, password, description));
	}

	@GetMapping("exeReceivedCPMT")
	@Timed
	public ResponseEntity<String> exeReceivedCPMT(@RequestParam String serviceCode, @RequestParam String packageCode,
			@RequestParam String contents, @RequestParam String userName, @RequestParam String password)
			throws Exception {
		return ResponseEntity.ok().body(vasgateClient.exeReceivedCPMT(serviceCode, packageCode, contents, userName, password));
	}

	@GetMapping("receiverPackageReq")
	@Timed
	public ResponseEntity<String> receiverPackageReq(@RequestParam String isdn, @RequestParam String serviceCode,
			@RequestParam String startDatetime, @RequestParam String endDatetime, @RequestParam String groupCode,
			@RequestParam String commandCode, @RequestParam String packageCode, @RequestParam String sourceCode,
			@RequestParam String user, @RequestParam String password, @RequestParam String description)
			throws Exception {
		return ResponseEntity.ok().body(vasgateClient.receiverPackageReq(isdn, serviceCode, startDatetime, endDatetime,
				groupCode, commandCode, packageCode, sourceCode, user, password, description));
	}

	@GetMapping("confirmMinusMoney")
	@Timed
	public ResponseEntity<String> confirmMinusMoney(@RequestParam String transactionId, @RequestParam String otp,
			@RequestParam String userName, @RequestParam String password) throws Exception {
		return ResponseEntity.ok().body(vasgateClient.confirmMinusMoney(transactionId, otp, userName, password));
	}

	@GetMapping("getInfomationCcgw")
	@Timed
	public ResponseEntity<String> getInfomationCcgw(@RequestParam String serviceCode, @RequestParam String isdn,
			@RequestParam String sourceCode, @RequestParam String userName, @RequestParam String password)
			throws Exception {
		return ResponseEntity.ok().body(vasgateClient.getInfomationCcgw(serviceCode, isdn, sourceCode, userName, password));
	}
}
