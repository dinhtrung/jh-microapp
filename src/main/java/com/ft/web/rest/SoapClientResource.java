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

import tns.ns.Result;
import tns.ns.SOAPMCA;

/**
 * REST controller for managing Cdr.
 */
@RestController
@RequestMapping("/api/soap")
public class SoapClientResource {

	@Autowired
	ApplicationProperties props;
	
	@Autowired
    SOAPMCA LUClient;
    
    @Autowired
    SOAPMCA CLClient;
    
    @Autowired
    SOAPMCA ISDClient;
	
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
    public ResponseEntity<Result> sendClearLocation(
    		@RequestParam String cdpa, 
    		@RequestParam String cgpa, 
    		@RequestParam String imsi, 
    		@RequestParam String tid
    ) throws Exception {
		Result resp = CLClient.sendCL(tid, imsi, cdpa, cgpa);
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
    public ResponseEntity<Result> sendInsertSubscriberData(
    		@RequestParam String msisdn, 
    		@RequestParam String tid
    ) throws Exception {
		Result resp = ISDClient.sendISD(tid, msisdn);
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
    public ResponseEntity<Result> sendLocationUpdate(
    		@RequestParam String cdpa, 
    		@RequestParam String cgpa, 
    		@RequestParam String imsi, 
    		@RequestParam String tid
    ) throws Exception {
		Result resp = LUClient.sendLU(tid, imsi, cdpa, cgpa);
		return ResponseEntity.ok(resp);
    }
}
