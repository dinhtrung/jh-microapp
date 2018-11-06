package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.config.ApplicationProperties;
import com.ft.domain.DataFile;
import com.ft.domain.Product;
import com.ft.service.SubProductService;
import com.ft.service.util.BeanUtils;
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
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class SubProductResource {

    private final Logger log = LoggerFactory.getLogger(SubProductResource.class);

    private static final String ENTITY_NAME = "subProduct";

    private final SubProductService subProductService;

    private final ApplicationProperties props;

    public SubProductResource(SubProductService subProductService, ApplicationProperties props) {
        this.subProductService = subProductService;
        this.props = props;
    }

    /**
     * POST  /sub-products : Create a new subProduct.
     *
     * @param subProduct the subProduct to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subProduct, or with status 400 (Bad Request) if the subProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-products")
    @Timed
    public ResponseEntity<Product> createSubProduct(@Valid @RequestBody Product subProduct) throws URISyntaxException {
        log.debug("REST request to save SubProduct : {}", subProduct);
        if (subProduct.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new subProduct cannot already have an ID")).body(null);
        }
        Product result = subProductService.save(subProduct);
        return ResponseEntity.created(new URI("/api/sub-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sub-products : Updates an existing subProduct.
     *
     * @param subProduct the subProduct to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subProduct,
     * or with status 400 (Bad Request) if the subProduct is not valid,
     * or with status 500 (Internal Server Error) if the subProduct couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sub-products")
    @Timed
    public ResponseEntity<Product> updateSubProduct(@Valid @RequestBody Product subProduct) throws URISyntaxException {
        log.debug("REST request to update SubProduct : {}", subProduct);
        if (subProduct.getId() == null) {
            return createSubProduct(subProduct);
        }
        Product result = subProductService.save(subProduct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subProduct.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sub-products : get all the subProducts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of subProducts in body
     */
    @GetMapping("/sub-products")
    @Timed
    public ResponseEntity<List<Product>> getAllSubProducts(@ApiParam Pageable pageable, @ApiParam @RequestParam(name = "query", required = false) String query) {
        Page<Product> page = subProductService.findAll(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sub-products/:id : get the "id" subProduct.
     *
     * @param id the id of the subProduct to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subProduct, or with status 404 (Not Found)
     */
    @GetMapping("/sub-products/{id}")
    @Timed
    public ResponseEntity<Product> getSubProduct(@PathVariable String id) {
    	if (id.equalsIgnoreCase("0")) return ResponseUtil.wrapOrNotFound(Optional.ofNullable(props.getProductTpl()));
        log.debug("REST request to get SubProduct : {}", id);
        return ResponseUtil.wrapOrNotFound(subProductService.findOne(id));
    }

    /**
     * DELETE  /sub-products/:id : delete the "id" subProduct.
     *
     * @param id the id of the subProduct to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sub-products/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubProduct(@PathVariable String id) {
        log.debug("REST request to delete SubProduct : {}", id);
        subProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sub-product-import")
    @Timed
    public ResponseEntity<DataFile> importData(@RequestBody DataFile dataFile) throws URISyntaxException {
        int result = subProductService.importData(dataFile);
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
    @GetMapping("/sub-product-export")
    @Timed
    public ResponseEntity<byte[]> exportData() {
        DataFile file = subProductService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file.getDataFile()));
    }

    @GetMapping("/clone-product")
    public ResponseEntity<Product> cloneProduct() {
    	Product src = props.getProductTpl();
    	Product target = new Product().code("Zance").status(true);
    	log.debug("Source Object: " + src + " Target " + target);
    	BeanUtils.copyPropertiesNotNull(src, target);
    	log.debug("Merged One: " + target);
    	return ResponseEntity.ok().body(target);
    }
}
