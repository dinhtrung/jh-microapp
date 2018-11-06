package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.SmsLog;
import com.ft.repository.SmsLogRepository;
import com.ft.service.SmsLogService;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmsLog.
 */
@RestController
@RequestMapping("/api")
public class SmsLogResource {

    private final Logger log = LoggerFactory.getLogger(SmsLogResource.class);

    private static final String ENTITY_NAME = "smsLog";

    private final SmsLogService smsLogService;
    
    private final SmsLogRepository smsRepo;
    
    public SmsLogResource(SmsLogService smsLogService, SmsLogRepository smsRepo, ObjectMapper mapper) {
        this.smsLogService = smsLogService;
        this.smsRepo = smsRepo;
    }

    /**
     * POST  /sms-logs : Create a new smsLog.
     *
     * @param smsLog the smsLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsLog, or with status 400 (Bad Request) if the smsLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-logs")
    @Timed
    public ResponseEntity<SmsLog> createSmsLog(@Valid @RequestBody SmsLog smsLog) throws URISyntaxException {
        log.debug("REST request to save SmsLog : {}", smsLog);
        if (smsLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new smsLog cannot already have an ID")).body(null);
        }
        SmsLog result = smsLogService.save(smsLog);
        return ResponseEntity.created(new URI("/api/sms-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-logs : Updates an existing smsLog.
     *
     * @param smsLog the smsLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsLog,
     * or with status 400 (Bad Request) if the smsLog is not valid,
     * or with status 500 (Internal Server Error) if the smsLog couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-logs")
    @Timed
    public ResponseEntity<SmsLog> updateSmsLog(@Valid @RequestBody SmsLog smsLog) throws URISyntaxException {
        log.debug("REST request to update SmsLog : {}", smsLog);
        if (smsLog.getId() == null) {
            return createSmsLog(smsLog);
        }
        SmsLog result = smsLogService.save(smsLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-logs : get all the smsLogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsLogs in body
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @GetMapping("/sms-logs")
    @Timed
    public ResponseEntity<List<SmsLog>> getAllSmsLogs(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        log.debug("REST request to get a page of SmsLogs -- query: " + query);
        Page<SmsLog> page = smsLogService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sms-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sms-logs : get all the smsLogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsLogs in body
     */
    @GetMapping("/sms-stats")
    @Timed
    public ResponseEntity<List<SmsLog>> statSmsLogs(@ApiParam Pageable pageable) {
        List<SmsLog> result = smsRepo.stats();
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /sms-logs/:id : get the "id" smsLog.
     *
     * @param id the id of the smsLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsLog, or with status 404 (Not Found)
     */
    @GetMapping("/sms-logs/{id}")
    @Timed
    public ResponseEntity<SmsLog> getSmsLog(@PathVariable String id) {
        log.debug("REST request to get SmsLog : {}", id);
        return ResponseUtil.wrapOrNotFound(smsLogService.findOne(id));
    }

    /**
     * DELETE  /sms-logs/:id : delete the "id" smsLog.
     *
     * @param id the id of the smsLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmsLog(@PathVariable String id) {
        log.debug("REST request to delete SmsLog : {}", id);
        smsLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
