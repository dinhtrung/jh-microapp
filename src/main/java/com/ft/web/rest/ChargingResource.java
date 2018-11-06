package com.ft.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.annotation.Timed;
import com.ft.config.ApplicationProperties;
import com.ft.web.api.model.ChargeRequest;
import com.ft.web.api.model.ChargeResponse;
import com.ft.web.rest.util.HeaderUtil;


/**
 * REST controller for managing Dnd.
 */
@RestController
@RequestMapping("/api")
public class ChargingResource {

    private final Logger log = LoggerFactory.getLogger(ChargingResource.class);

    private static final String ENTITY_NAME = "charging";
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    ApplicationProperties props;

    /**
     * POST  /dnds : Create a new dnd.
     *
     * @param dnd the dnd to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dnd, or with status 400 (Bad Request) if the dnd has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/charging")
    @Timed
    public ResponseEntity<ChargeResponse> createDnd(@Valid @RequestBody ChargeRequest req) throws URISyntaxException {
    	try {
    		ResponseEntity<ChargeResponse> result = restTemplate.postForEntity(props.getChargingUrl(), req, ChargeResponse.class);
    		return ResponseEntity.created(new URI("/api/charging/" + result.getBody().getTranxId()))
    	            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getBody().getTranxId()))
    	            .body(result.getBody());
    	} catch (HttpStatusCodeException e) {
    		log.error("RESPONSE: {} -- {} | {} ", e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString());
    	} catch (Exception f ) {
    		log.error("Cannot send message {}", f);
    	}
    	return ResponseEntity.badRequest().body(null);
    }
}
