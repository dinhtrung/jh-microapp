package com.ft.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ft.domain.Cdr;
import com.ft.repository.CdrRepository;


/**
 * Service Implementation for managing Cdr.
 */
@Service
public class CdrService {

    private final Logger log = LoggerFactory.getLogger(CdrService.class);

    private final CdrRepository cdrRepository;
    public CdrService(CdrRepository cdrRepository) {
        this.cdrRepository = cdrRepository;
    }

    /**
     * Save a cdr.
     *
     * @param cdr the entity to save
     * @return the persisted entity
     */
    public Cdr save(Cdr cdr) {
        log.debug("Request to save Cdr : {}", cdr);
        return cdrRepository.save(cdr);
    }

    /**
     *  Get all the cdrs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Cdr> findAll(Pageable pageable) {
        log.debug("Request to get all Cdrs");
        return cdrRepository.findAll(pageable);
    }
    
    /**
     * @param searchModel JSON encoded values of Cdr search Model
     * @param pageable
     * @return
     */
	public Page<Cdr> findAll(String searchModel, Pageable pageable) {
		return (searchModel != null) ? cdrRepository.findAll(searchModel, pageable) : findAll(pageable) ;
	}
	
	/**
     * @param searchModel JSON encoded values of Cdr search Model
     * @param pageable
     * @return
     */
	public Page<Cdr> findAll(Cdr searchModel, Pageable pageable) {
		return (searchModel != null) ? cdrRepository.findAll(cdrRepository.createQuery(searchModel), pageable) : findAll(pageable) ;
	}


    /**
     *  Get one cdr by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<Cdr> findOne(String id) {
        log.debug("Request to get Cdr : {}", id);
        return cdrRepository.findById(id);
    }

    /**
     *  Delete the  cdr by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Cdr : {}", id);
        cdrRepository.deleteById(id);
    }

	public List<Cdr> stats(String query) {
		log.debug("Stats based on query: " + query);
		return cdrRepository.stats(query);
	}
}
