package com.ft.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ft.domain.Sms;
import com.ft.repository.SmsRepository;


/**
 * Service Implementation for managing Sms.
 */
@Service
public class SmsService {

    private final Logger log = LoggerFactory.getLogger(SmsService.class);

    private final SmsRepository smsRepository;
    public SmsService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    /**
     * Save a sms.
     *
     * @param sms the entity to save
     * @return the persisted entity
     */
    public Sms save(Sms sms) {
        log.debug("Request to save Sms : {}", sms);
        return smsRepository.save(sms);
    }

    /**
     *  Get all the sms.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Sms> findAll(Pageable pageable) {
        log.debug("Request to get all Sms");
        return smsRepository.findAll(pageable);
    }

    /**
     *  Get one sms by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<Sms> findOne(String id) {
        log.debug("Request to get Sms : {}", id);
        return smsRepository.findById(id);
    }

    /**
     *  Delete the  sms by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Sms : {}", id);
        smsRepository.deleteById(id);
    }
}
