package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.DataFile;
import com.ft.domain.SmsContent;
import com.ft.repository.SmsContentRepository;
import com.ft.service.SmsContentService;
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
 * REST controller for managing SmsContent.
 */
@RestController
@RequestMapping("/api")
public class SmsContentResource {

    private final Logger log = LoggerFactory.getLogger(SmsContentResource.class);

    private static final String ENTITY_NAME = "smsContent";

    private final SmsContentService smsContentService;

    private final SmsContentRepository msgRepo;

    public SmsContentResource(SmsContentService smsContentService, SmsContentRepository msgRepo) {
        this.smsContentService = smsContentService;
        this.msgRepo = msgRepo;
    }

    /**
     * POST  /sms-contents : Create a new smsContent.
     *
     * @param smsContent the smsContent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsContent, or with status 400 (Bad Request) if the smsContent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-contents")
    @Timed
    public ResponseEntity<SmsContent> createSmsContent(@Valid @RequestBody SmsContent smsContent) throws URISyntaxException {
        log.debug("REST request to save SmsContent : {}", smsContent);
        if (smsContent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new smsContent cannot already have an ID")).body(null);
        }
        SmsContent result = smsContentService.save(smsContent);
        return ResponseEntity.created(new URI("/api/sms-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-contents : Updates an existing smsContent.
     *
     * @param smsContent the smsContent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsContent,
     * or with status 400 (Bad Request) if the smsContent is not valid,
     * or with status 500 (Internal Server Error) if the smsContent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-contents")
    @Timed
    public ResponseEntity<SmsContent> updateSmsContent(@Valid @RequestBody SmsContent smsContent) throws URISyntaxException {
        log.debug("REST request to update SmsContent : {}", smsContent);
        if (smsContent.getId() == null) {
            return createSmsContent(smsContent);
        }
        SmsContent result = smsContentService.save(smsContent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsContent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-contents : get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsContents in body
     */
    @GetMapping("/sms-contents")
    @Timed
    public ResponseEntity<List<SmsContent>> getAllSmsContents(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        log.debug("REST request to get a page of SmsContents");
        Page<SmsContent> page = smsContentService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sms-contents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sms-contents : get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsContents in body
     */
    @GetMapping("/today-contents")
    @Timed
    public ResponseEntity<List<SmsContent>> getTodaySmsContents() {
        log.debug("REST request to get a page of SmsContents");
        List<SmsContent> page = smsContentService.getToday();
        return ResponseEntity.ok().body(page);
    }
    /**
     * GET  /sms-contents : get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsContents in body
     */
    @GetMapping("/submit-contents")
    @Timed
    public ResponseEntity<Integer> submitTodaySmsContents() {
        Integer page = smsContentService.submitToday();
        return ResponseEntity.ok().body(page);
    }

    /**
     * GET  /sms-contents : get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsContents in body
     */
    @GetMapping("/product-contents/{id}")
    @Timed
    public ResponseEntity<List<SmsContent>> getProductContents(@PathVariable String id, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SmsContents");
        Page<SmsContent> page = smsContentService.getProductContent(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/product-contents/" + id);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sms-contents : get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsContents in body
     */
    @GetMapping("/content-stats")
    @Timed
    public ResponseEntity<List<SmsContent>> statsContents() {
    	List<SmsContent> result = msgRepo.stats(null);
        return ResponseEntity.ok().body(result);
    }
    /**
     * GET  /sms-contents : get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of smsContents in body
     */
    @PostMapping("/content-stats")
    @Timed
    public ResponseEntity<List<SmsContent>> statsContents(@RequestBody SmsContent search) {
    	List<SmsContent> result = msgRepo.stats(search);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET  /sms-contents/:id : get the "id" smsContent.
     *
     * @param id the id of the smsContent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsContent, or with status 404 (Not Found)
     */
    @GetMapping("/sms-contents/{id}")
    @Timed
    public ResponseEntity<SmsContent> getSmsContent(@PathVariable String id) {
        log.debug("REST request to get SmsContent : {}", id);
        return ResponseUtil.wrapOrNotFound(smsContentService.findOne(id));
    }

    /**
     * DELETE  /sms-contents/:id : delete the "id" smsContent.
     *
     * @param id the id of the smsContent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmsContent(@PathVariable String id) {
        log.debug("REST request to delete SmsContent : {}", id);
        smsContentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-content-import")
    @Timed
    public ResponseEntity<DataFile> importData(@RequestBody DataFile dataFile) throws URISyntaxException {
        int result = smsContentService.importData(dataFile);
        return ResponseEntity.accepted()
        		.headers(HeaderUtil.createEntityUpdateAlert("dataFile", String.valueOf(result)))
                .body(dataFile);
    }

    /**
     * GET  /sms-content/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     */
    @GetMapping("/sms-content-export")
    @Timed
    public ResponseEntity<byte[]> exportData() {
        DataFile file = smsContentService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file.getDataFile()));
    }
}
