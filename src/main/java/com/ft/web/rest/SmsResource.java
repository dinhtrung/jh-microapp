package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Sms;
import com.ft.service.SmsService;
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
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sms.
 */
@RestController
@RequestMapping("/api")
public class SmsResource {

    private final Logger log = LoggerFactory.getLogger(SmsResource.class);

    private static final String ENTITY_NAME = "sms";

    private final SmsService smsService;

    public SmsResource(SmsService smsService) {
        this.smsService = smsService;
    }

    /**
     * POST  /sms : Create a new sms.
     *
     * @param sms the sms to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sms, or with status 400 (Bad Request) if the sms has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms")
    @Timed
    public ResponseEntity<Sms> createSms(@Valid @RequestBody Sms sms) throws URISyntaxException {
        log.debug("REST request to save Sms : {}", sms);
        if (sms.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new sms cannot already have an ID")).body(null);
        }
        Sms result = smsService.save(sms);
        return ResponseEntity.created(new URI("/api/sms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms : Updates an existing sms.
     *
     * @param sms the sms to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sms,
     * or with status 400 (Bad Request) if the sms is not valid,
     * or with status 500 (Internal Server Error) if the sms couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms")
    @Timed
    public ResponseEntity<Sms> updateSms(@Valid @RequestBody Sms sms) throws URISyntaxException {
        log.debug("REST request to update Sms : {}", sms);
        if (sms.getId() == null) {
            return createSms(sms);
        }
        Sms result = smsService.save(sms);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sms.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms : get all the sms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sms in body
     */
    @GetMapping("/sms")
    @Timed
    public ResponseEntity<List<Sms>> getAllSms(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Sms");
        Page<Sms> page = smsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sms/:id : get the "id" sms.
     *
     * @param id the id of the sms to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sms, or with status 404 (Not Found)
     */
    @GetMapping("/sms/{id}")
    @Timed
    public ResponseEntity<Sms> getSms(@PathVariable String id) {
        log.debug("REST request to get Sms : {}", id);
        return ResponseUtil.wrapOrNotFound(smsService.findOne(id));
    }

    /**
     * DELETE  /sms/:id : delete the "id" sms.
     *
     * @param id the id of the sms to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms/{id}")
    @Timed
    public ResponseEntity<Void> deleteSms(@PathVariable String id) {
        log.debug("REST request to delete Sms : {}", id);
        smsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
