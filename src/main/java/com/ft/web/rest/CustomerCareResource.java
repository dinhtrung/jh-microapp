package com.ft.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ft.service.SubMsisdnService;
import com.ft.service.SubscriptionService;
import com.ft.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Subscriber.
 */
@RestController
@RequestMapping("/api")
public class CustomerCareResource {

    private final Logger log = LoggerFactory.getLogger(CustomerCareResource.class);

    private static final String ENTITY_NAME = "subMsisdn";

    private final SubMsisdnService subMsisdnService;

    private final SubscriptionService subscriptionService;

    public CustomerCareResource(SubMsisdnService subMsisdnService, SubscriptionService subscriptionService) {
        this.subMsisdnService = subMsisdnService;
        this.subscriptionService = subscriptionService;
    }

    /**
     * DELETE  /sub-msisdns/:id : delete the "id" subMsisdn.
     *
     * @param id the id of the subMsisdn to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-care/subscription/{id}")
    @Timed
    public ResponseEntity<Void> deleteSubMsisdn(@PathVariable String id) {
        log.debug("REST request to delete SubMsisdn : {}", id);
        subscriptionService.unsub(subMsisdnService.findOne(id).get());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
