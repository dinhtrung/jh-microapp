package com.ft.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.CdrStat;
import com.ft.service.CdrStatService;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing CdrStat.
 */
@RestController
@RequestMapping("/api")
public class CdrStatResource {

    private final Logger log = LoggerFactory.getLogger(CdrStatResource.class);

    private static final String ENTITY_NAME = "cdrStat";

    private final CdrStatService cdrStatService;

    public CdrStatResource(CdrStatService cdrStatService) {
        this.cdrStatService = cdrStatService;
    }

    /**
     * POST  /cdr-stats : Create a new cdrStat.
     *
     * @param cdrStat the cdrStat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cdrStat, or with status 400 (Bad Request) if the cdrStat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cdr-stats")
    @Timed
    public ResponseEntity<CdrStat> createCdrStat(@Valid @RequestBody CdrStat cdrStat) throws URISyntaxException {
        log.debug("REST request to save CdrStat : {}", cdrStat);
        if (cdrStat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cdrStat cannot already have an ID")).body(null);
        }
        CdrStat result = cdrStatService.save(cdrStat);
        return ResponseEntity.created(new URI("/api/cdr-stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cdr-stats : Updates an existing cdrStat.
     *
     * @param cdrStat the cdrStat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cdrStat,
     * or with status 400 (Bad Request) if the cdrStat is not valid,
     * or with status 500 (Internal Server Error) if the cdrStat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cdr-stats")
    @Timed
    public ResponseEntity<CdrStat> updateCdrStat(@Valid @RequestBody CdrStat cdrStat) throws URISyntaxException {
        log.debug("REST request to update CdrStat : {}", cdrStat);
        if (cdrStat.getId() == null) {
            return createCdrStat(cdrStat);
        }
        CdrStat result = cdrStatService.save(cdrStat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cdrStat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cdr-stats : get all the cdrStats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cdrStats in body
     */
    @GetMapping("/cdr-stats")
    @Timed
    public ResponseEntity<List<CdrStat>> getAllCdrStats(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        log.debug("REST request to get a page of CdrStats");
        Page<CdrStat> page = cdrStatService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cdr-stats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cdr-stats/:id : get the "id" cdrStat.
     *
     * @param id the id of the cdrStat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cdrStat, or with status 404 (Not Found)
     */
    @GetMapping("/cdr-stats/{id}")
    @Timed
    public ResponseEntity<CdrStat> getCdrStat(@PathVariable String id) {
        log.debug("REST request to get CdrStat : {}", id);
        return ResponseUtil.wrapOrNotFound(cdrStatService.findOne(id));
    }

    /**
     * DELETE  /cdr-stats/:id : delete the "id" cdrStat.
     *
     * @param id the id of the cdrStat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cdr-stats/{id}")
    @Timed
    public ResponseEntity<Void> deleteCdrStat(@PathVariable String id) {
        log.debug("REST request to delete CdrStat : {}", id);
        cdrStatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    @GetMapping("/get-cdr-stats")
    @Timed
    public ResponseEntity<List<CdrStat>> statsCdrs(@ApiParam @RequestParam(name = "query", required = false) String query) {
    	List<CdrStat> result = cdrStatService.getStatFromCdr();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/get-dashboard")
    @Timed
    public ResponseEntity<Map<String, Object>> getDashboard  (
    		@ApiParam @RequestParam(name = "from", required = false) ZonedDateTime from,
    		@ApiParam @RequestParam(name = "to", required = false) ZonedDateTime to
    		) throws Exception {
    	if (from == null) from = ZonedDateTime.now().withDayOfMonth(1);
    	if (to == null) to = ZonedDateTime.now();
		return ResponseEntity.ok().body(cdrStatService.getDashboard(from, to));
    }
}
