package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Stat;
import com.ft.service.StatService;
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
 * REST controller for managing Stat.
 */
@RestController
@RequestMapping("/api")
public class StatResource {

    private final Logger log = LoggerFactory.getLogger(StatResource.class);

    private static final String ENTITY_NAME = "stat";

    private final StatService statService;

    public StatResource(StatService statService) {
        this.statService = statService;
    }

    /**
     * POST  /stats : Create a new stat.
     *
     * @param stat the stat to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stat, or with status 400 (Bad Request) if the stat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stats")
    @Timed
    public ResponseEntity<Stat> createStat(@Valid @RequestBody Stat stat) throws URISyntaxException {
        log.debug("REST request to save Stat : {}", stat);
        if (stat.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new stat cannot already have an ID")).body(null);
        }
        Stat result = statService.save(stat);
        return ResponseEntity.created(new URI("/api/stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stats : Updates an existing stat.
     *
     * @param stat the stat to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stat,
     * or with status 400 (Bad Request) if the stat is not valid,
     * or with status 500 (Internal Server Error) if the stat couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stats")
    @Timed
    public ResponseEntity<Stat> updateStat(@Valid @RequestBody Stat stat) throws URISyntaxException {
        log.debug("REST request to update Stat : {}", stat);
        if (stat.getId() == null) {
            return createStat(stat);
        }
        Stat result = statService.save(stat);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stat.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stats : get all the stats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stats in body
     */
    @GetMapping("/stats")
    @Timed
    public ResponseEntity<List<Stat>> getAllStats(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Stats");
        Page<Stat> page = statService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stats/:id : get the "id" stat.
     *
     * @param id the id of the stat to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stat, or with status 404 (Not Found)
     */
    @GetMapping("/stats/{id}")
    @Timed
    public ResponseEntity<Stat> getStat(@PathVariable String id) {
        log.debug("REST request to get Stat : {}", id);
        return ResponseUtil.wrapOrNotFound(statService.findOne(id));
    }

    /**
     * DELETE  /stats/:id : delete the "id" stat.
     *
     * @param id the id of the stat to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stats/{id}")
    @Timed
    public ResponseEntity<Void> deleteStat(@PathVariable String id) {
        log.debug("REST request to delete Stat : {}", id);
        statService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
