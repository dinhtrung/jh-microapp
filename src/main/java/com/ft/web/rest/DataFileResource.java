package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.DataFile;
import com.ft.service.DataFileService;
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
 * REST controller for managing DataFile.
 */
@RestController
@RequestMapping("/api")
public class DataFileResource {

    private final Logger log = LoggerFactory.getLogger(DataFileResource.class);

    private static final String ENTITY_NAME = "dataFile";

    private final DataFileService dataFileService;

    public DataFileResource(DataFileService dataFileService) {
        this.dataFileService = dataFileService;
    }

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-files")
    @Timed
    public ResponseEntity<DataFile> createDataFile(@RequestBody DataFile dataFile) throws URISyntaxException {
        log.debug("REST request to save DataFile : {}", dataFile);
        if (dataFile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new dataFile cannot already have an ID")).body(null);
        }
        DataFile result = dataFileService.save(dataFile);
        return ResponseEntity.created(new URI("/api/data-files/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-files : Updates an existing dataFile.
     *
     * @param dataFile the dataFile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataFile,
     * or with status 400 (Bad Request) if the dataFile is not valid,
     * or with status 500 (Internal Server Error) if the dataFile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-files")
    @Timed
    public ResponseEntity<DataFile> updateDataFile(@RequestBody DataFile dataFile) throws URISyntaxException {
        log.debug("REST request to update DataFile : {}", dataFile);
        if (dataFile.getId() == null) {
            return createDataFile(dataFile);
        }
        DataFile result = dataFileService.save(dataFile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataFile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-files : get all the dataFiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dataFiles in body
     */
    @GetMapping("/data-files")
    @Timed
    public ResponseEntity<List<DataFile>> getAllDataFiles(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of DataFiles");
        Page<DataFile> page = dataFileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-files");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /data-files/:id : get the "id" dataFile.
     *
     * @param id the id of the dataFile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataFile, or with status 404 (Not Found)
     */
    @GetMapping("/data-files/{id}")
    @Timed
    public ResponseEntity<DataFile> getDataFile(@PathVariable String id) {
        log.debug("REST request to get DataFile : {}", id);
        return ResponseUtil.wrapOrNotFound(dataFileService.findOne(id));
    }

    /**
     * DELETE  /data-files/:id : delete the "id" dataFile.
     *
     * @param id the id of the dataFile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-files/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataFile(@PathVariable String id) {
        log.debug("REST request to delete DataFile : {}", id);
        dataFileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
