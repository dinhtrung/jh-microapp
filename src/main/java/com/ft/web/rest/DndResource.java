package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.DataFile;
import com.ft.domain.Dnd;
import com.ft.service.DndService;
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
 * REST controller for managing Dnd.
 */
@RestController
@RequestMapping("/api")
public class DndResource {

    private final Logger log = LoggerFactory.getLogger(DndResource.class);

    private static final String ENTITY_NAME = "dnd";

    private final DndService dndService;

    public DndResource(DndService dndService) {
        this.dndService = dndService;
    }

    /**
     * POST  /dnds : Create a new dnd.
     *
     * @param dnd the dnd to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dnd, or with status 400 (Bad Request) if the dnd has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dnds")
    @Timed
    public ResponseEntity<Dnd> createDnd(@Valid @RequestBody Dnd dnd) throws URISyntaxException {
        log.debug("REST request to save Dnd : {}", dnd);
        if (dnd.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dnd cannot already have an ID")).body(null);
        }
        Dnd result = dndService.save(dnd);
        return ResponseEntity.created(new URI("/api/dnds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dnds : Updates an existing dnd.
     *
     * @param dnd the dnd to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dnd,
     * or with status 400 (Bad Request) if the dnd is not valid,
     * or with status 500 (Internal Server Error) if the dnd couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dnds")
    @Timed
    public ResponseEntity<Dnd> updateDnd(@Valid @RequestBody Dnd dnd) throws URISyntaxException {
        log.debug("REST request to update Dnd : {}", dnd);
        if (dnd.getId() == null) {
            return createDnd(dnd);
        }
        Dnd result = dndService.save(dnd);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dnd.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dnds : get all the dnds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dnds in body
     */
    @GetMapping("/dnds")
    @Timed
    public ResponseEntity<List<Dnd>> getAllDnds(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        log.debug("REST request to get a page of Dnds");
        Page<Dnd> page = dndService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dnds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dnds/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/dnds/{id}")
    @Timed
    public ResponseEntity<Dnd> getDnd(@PathVariable String id) {
        log.debug("REST request to get Dnd : {}", id);
        return ResponseUtil.wrapOrNotFound(dndService.findOne(id));
    }

    /**
     * DELETE  /dnds/:id : delete the "id" dnd.
     *
     * @param id the id of the dnd to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dnds/{id}")
    @Timed
    public ResponseEntity<Void> deleteDnd(@PathVariable String id) {
        log.debug("REST request to delete Dnd : {}", id);
        dndService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
    
    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dnds-import")
    @Timed
    public ResponseEntity<DataFile> importDnd(@RequestBody DataFile dataFile) throws URISyntaxException {
        int result = dndService.importData(dataFile);
        return ResponseEntity.accepted()
        		.headers(HeaderUtil.createEntityUpdateAlert("dataFile", String.valueOf(result)))
                .body(dataFile);
    }
    
    /**
     * GET  /dnds/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/dnds-export-file")
    @Timed
    public ResponseEntity<DataFile> exportDnd() {
        DataFile dnd = dndService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dnd));
    }
    
    /**
     * GET  /dnds/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/dnds-export")
    @Timed
    public ResponseEntity<byte[]> exportBlob() {
        DataFile dnd = dndService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dnd.getDataFile()));
    }
}
