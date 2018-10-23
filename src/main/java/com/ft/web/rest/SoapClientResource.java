package com.ft.web.rest;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ft.config.ApplicationProperties;

import ns.SOAP_MCAStub;
import ns.SendCLDocument;
import ns.SendCLDocument.SendCL;
import ns.SendCLResponseDocument;
import ns.SendISDDocument;
import ns.SendISDDocument.SendISD;
import ns.SendISDResponseDocument;
import ns.SendLUDocument;
import ns.SendLUDocument.SendLU;
import ns.SendLUResponseDocument;

/**
 * REST controller for managing Cdr.
 */
@RestController
@RequestMapping("/api/soap")
public class SoapClientResource {

	@Autowired
	ApplicationProperties props;
	
	/**
     * GET  /sub-products/:id : get the "id" subProduct.
     *
     * @param id the id of the subProduct to retrieve
	 * @param cdpa 
	 * @param cgpa 
	 * @param imsi 
	 * @param tid 
     * @return the ResponseEntity with status 200 (OK) and with body the subProduct, or with status 404 (Not Found)
	 * @throws RemoteException 
     */
    @GetMapping("/cancel-location")
    @Timed
    public ResponseEntity<SendCLResponseDocument> sendClearLocation(
    		@RequestParam String cdpa, 
    		@RequestParam String cgpa, 
    		@RequestParam String imsi, 
    		@RequestParam String tid
    ) throws Exception {
		SendCLDocument doc = SendCLDocument.Factory.newInstance();
		SendCL evt = doc.addNewSendCL();
		evt.setCdpa(cdpa);
		evt.setCgpa(cgpa);
		evt.setImsi(imsi);
		evt.setTid(tid);
		SOAP_MCAStub stub = new SOAP_MCAStub(props.getServerUrl());
		SendCLResponseDocument resp = stub.sendCL(doc);
		return ResponseEntity.ok(resp);
    }
    
    
    /**
     * GET  /sub-products/:id : get the "id" subProduct.
     *
     * @param id the id of the subProduct to retrieve
	 * @param cdpa 
	 * @param cgpa 
	 * @param imsi 
	 * @param tid 
     * @return the ResponseEntity with status 200 (OK) and with body the subProduct, or with status 404 (Not Found)
	 * @throws RemoteException 
     */
    @GetMapping("/insert-subscriber-data")
    @Timed
    public ResponseEntity<SendISDResponseDocument> sendInsertSubscriberData(
    		@RequestParam String msisdn, 
    		@RequestParam String tid
    ) throws Exception {
		SendISDDocument doc = SendISDDocument.Factory.newInstance();
		SendISD evt = doc.addNewSendISD();
		evt.setTid(tid);
		evt.setMsisdn(msisdn);
		SOAP_MCAStub stub = new SOAP_MCAStub(props.getServerUrl());
		SendISDResponseDocument resp = stub.sendISD(doc);
		return ResponseEntity.ok(resp);
    }
    
    
    /**
     * GET  /sub-products/:id : get the "id" subProduct.
     *
     * @param id the id of the subProduct to retrieve
	 * @param cdpa 
	 * @param cgpa 
	 * @param imsi 
	 * @param tid 
     * @return the ResponseEntity with status 200 (OK) and with body the subProduct, or with status 404 (Not Found)
	 * @throws RemoteException 
     */
    @GetMapping("/location-update")
    @Timed
    public ResponseEntity<SendLUResponseDocument> sendLocationUpdate(
    		@RequestParam String cdpa, 
    		@RequestParam String cgpa, 
    		@RequestParam String imsi, 
    		@RequestParam String tid
    ) throws Exception {
		SendLUDocument doc = SendLUDocument.Factory.newInstance();
		SendLU evt = doc.addNewSendLU();
		evt.setCdpa(cdpa);
		evt.setCgpa(cgpa);
		evt.setImsi(imsi);
		evt.setTid(tid);
		SOAP_MCAStub stub = new SOAP_MCAStub(props.getServerUrl());
		SendLUResponseDocument resp = stub.sendLU(doc);
		return ResponseEntity.ok(resp);
    }
}
