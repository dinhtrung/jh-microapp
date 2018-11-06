package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.DataFile;
import com.ft.domain.Subscriber;
import com.ft.service.SubMsisdnService;
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
 * REST controller for managing Subscriber.
 */
@RestController
@RequestMapping("/api")
public class SubMsisdnResource {

    private final Logger log = LoggerFactory.getLogger(SubMsisdnResource.class);

    private static final String ENTITY_NAME = "subMsisdn";

    private final SubMsisdnService subMsisdnService;

    public SubMsisdnResource(SubMsisdnService subMsisdnService) {
        this.subMsisdnService = subMsisdnService;
    }

    /**
     * POST  /sub-msisdns : Create a new subMsisdn.
     *
     * @param subscriber the subMsisdn to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subMsisdn, or with status 400 (Bad Request) if the subMsisdn has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-msisdns")
    @Timed
    public ResponseEntity<Subscriber> createSubMsisdn(@Valid @RequestBody Subscriber subscriber) throws URISyntaxException {
        log.debug("REST request to save SubMsisdn : {}", subscriber);
        if (subscriber.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new subMsisdn cannot already have an ID")).body(null);
        }
        Subscriber result = subMsisdnService.save(subscriber);
        return ResponseEntity.created(new URI("/api/sub-msisdns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-msisdns : Updates an existing subMsisdn.
     *
     * @param subMsisdn the subMsisdn to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subMsisdn,
     * or with status 400 (Bad Request) if the subMsisdn is not valid,
     * or with status 500 (Internal Server Error) if the subMsisdn couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-msisdns")
    @Timed
    public ResponseEntity<Subscriber> updateSubMsisdn(@Valid @RequestBody Subscriber subMsisdn) throws URISyntaxException {
        log.debug("REST request to update SubMsisdn : {}", subMsisdn);
        if (subMsisdn.getId() == null) {
            return createSubMsisdn(subMsisdn);
        }
        Subscriber result = subMsisdnService.save(subMsisdn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subMsisdn.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-msisdns : get all the subMsisdns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subMsisdns in body
     */
    @GetMapping("/sub-msisdns")
    @Timed
    public ResponseEntity<List<Subscriber>> getAllSubMsisdns(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        log.debug("REST request to get a page of SubMsisdns");
        Page<Subscriber> page = subMsisdnService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-msisdns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sub-msisdns/:id : get the "id" subMsisdn.
     *
     * @param id the id of the subMsisdn to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subMsisdn, or with status 404 (Not Found)
     */
    @GetMapping("/sub-msisdns/{id}")
    @Timed
    public ResponseEntity<Subscriber> getSubMsisdn(@PathVariable String id) {
        log.debug("REST request to get SubMsisdn : {}", id);
        return ResponseUtil.wrapOrNotFound(subMsisdnService.findOne(id));
    }

    /**
     * DELETE  /sub-msisdns/:id : delete the "id" subMsisdn.
     *
     * @param id the id of the subMsisdn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-msisdns/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubMsisdn(@PathVariable String id) {
        log.debug("REST request to delete SubMsisdn : {}", id);
        subMsisdnService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-msisdn-import")
    @Timed
    public ResponseEntity<DataFile> importData(@RequestBody DataFile dataFile) throws URISyntaxException {
        int result = subMsisdnService.importData(dataFile);
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createEntityUpdateAlert("dataFile", String.valueOf(result)))
                .body(dataFile);
    }

    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/sub-msisdn-export/:id")
    @Timed
    public ResponseEntity<DataFile> exportData(@PathVariable("id") String id) {
        DataFile file = subMsisdnService.exportData(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file));
    }

    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/sub-msisdn-export")
    @Timed
    public ResponseEntity<byte[]> exportData() {
        DataFile file = subMsisdnService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file.getDataFile()));
    }
    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/active-msisdn")
    @Timed
    public ResponseEntity<List<Subscriber>> activeMsisdn(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SubMsisdns");
        Page<Subscriber> page = subMsisdnService.findAllActive(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/active-msisdn");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/sub-msisdn-await-notification")
    @Timed
    public ResponseEntity<List<Subscriber>> awaitNotificationSubMsisdn(@ApiParam Pageable pageable) {
        Page<Subscriber> page = subMsisdnService.findAllAwaitNotify(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-msisdn-await-notification");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/cleanup-msisdn")
    @Timed
    public ResponseEntity<Integer> cleanupMsisdn() {
    	return ResponseEntity.accepted().body(subMsisdnService.cleanup());
    }
}
