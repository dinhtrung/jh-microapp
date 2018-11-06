package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Cdr;
import com.ft.repository.CdrRepository;
import com.ft.service.CdrService;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cdr.
 */
@RestController
@RequestMapping("/api")
public class CdrResource {

    private final Logger log = LoggerFactory.getLogger(CdrResource.class);

    private static final String ENTITY_NAME = "cdr";

    private final CdrService cdrService;
    
    public CdrResource(CdrService cdrService, CdrRepository cdrRepo) {
        this.cdrService = cdrService;
    }

    /**
     * POST  /cdrs : Create a new cdr.
     *
     * @param cdr the cdr to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cdr, or with status 400 (Bad Request) if the cdr has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cdrs")
    @Timed
    public ResponseEntity<Cdr> createCdr(@RequestBody Cdr cdr) throws URISyntaxException {
        log.debug("REST request to save Cdr : {}", cdr);
        if (cdr.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cdr cannot already have an ID")).body(null);
        }
        Cdr result = cdrService.save(cdr);
        return ResponseEntity.created(new URI("/api/cdrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cdrs : Updates an existing cdr.
     *
     * @param cdr the cdr to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cdr,
     * or with status 400 (Bad Request) if the cdr is not valid,
     * or with status 500 (Internal Server Error) if the cdr couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cdrs")
    @Timed
    public ResponseEntity<Cdr> updateCdr(@RequestBody Cdr cdr) throws URISyntaxException {
        log.debug("REST request to update Cdr : {}", cdr);
        if (cdr.getId() == null) {
            return createCdr(cdr);
        }
        Cdr result = cdrService.save(cdr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cdr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cdrs : get all the cdrs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cdrs in body
     */
    @GetMapping("/cdrs")
    @Timed
    public ResponseEntity<List<Cdr>> getAllCdrs(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        log.debug("REST request to get a page of Cdrs");
        Page<Cdr> page = cdrService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cdrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cdrs : get all the cdrs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cdrs in body
     */
    @GetMapping("/cdr-statistic")
    @Timed
    public ResponseEntity<List<Cdr>> statsCdrs(@ApiParam @RequestParam(name = "query", required = false) String query) {
    	List<Cdr> result = cdrService.stats(query);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /cdrs/:id : get the "id" cdr.
     *
     * @param id the id of the cdr to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cdr, or with status 404 (Not Found)
     */
    @GetMapping("/cdrs/{id}")
    @Timed
    public ResponseEntity<Cdr> getCdr(@PathVariable String id) {
        log.debug("REST request to get Cdr : {}", id);
        return ResponseUtil.wrapOrNotFound(cdrService.findOne(id));
    }

    /**
     * DELETE  /cdrs/:id : delete the "id" cdr.
     *
     * @param id the id of the cdr to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cdrs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCdr(@PathVariable String id) {
        log.debug("REST request to delete Cdr : {}", id);
        cdrService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
