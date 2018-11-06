package com.ft.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ft.domain.SmsLog;
import com.ft.repository.SmsLogRepository;


/**
 * Service Implementation for managing SmsLog.
 */
@Service
public class SmsLogService {

    private final Logger log = LoggerFactory.getLogger(SmsLogService.class);

    private final SmsLogRepository smsLogRepository;
    public SmsLogService(SmsLogRepository smsLogRepository) {
        this.smsLogRepository = smsLogRepository;
    }

    /**
     * Save a smsLog.
     *
     * @param smsLog the entity to save
     * @return the persisted entity
     */
    public SmsLog save(SmsLog smsLog) {
        log.debug("Request to save SmsLog : {}", smsLog);
        return smsLogRepository.save(smsLog);
    }

    /**
     *  Get all the smsLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<SmsLog> findAll(Pageable pageable) {
        log.debug("Request to get all SmsLogs");
        return smsLogRepository.findAll(pageable);
    }

    /**
     *  Get one smsLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<SmsLog> findOne(String id) {
        log.debug("Request to get SmsLog : {}", id);
        return smsLogRepository.findById(id);
    }

    /**
     *  Delete the  smsLog by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete SmsLog : {}", id);
        smsLogRepository.deleteById(id);
    }
    
    
    /**
     * Delete pending SMS
     */
    public int delete(String msisdn, String productId){
    	return smsLogRepository.deleteByDestinationAndStatusLessThanAndProductId(msisdn, SmsLog.STATE_PENDING, productId);
    }

    /**
     * @param searchModel JSON encoded values of SmsLog search Model
     * @param pageable
     * @return
     */
	public Page<SmsLog> findAll(String searchModel, Pageable pageable) {
		return (searchModel != null) ? smsLogRepository.findAll(searchModel, pageable) : findAll(pageable) ;
	}
	
	/**
     * @param searchModel JSON encoded values of SmsLog search Model
     * @param pageable
     * @return
     */
	public Page<SmsLog> findAll(SmsLog searchModel, Pageable pageable) {
		return (searchModel != null) ? smsLogRepository.findAll(smsLogRepository.createQuery(searchModel), pageable) : findAll(pageable) ;
	}
}
