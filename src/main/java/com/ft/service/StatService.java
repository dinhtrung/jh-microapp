package com.ft.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ft.domain.Stat;
import com.ft.repository.StatRepository;


/**
 * Service Implementation for managing Stat.
 */
@Service
public class StatService {

    private final Logger log = LoggerFactory.getLogger(StatService.class);

    private final StatRepository statRepository;
    public StatService(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    /**
     * Save a stat.
     *
     * @param stat the entity to save
     * @return the persisted entity
     */
    public Stat save(Stat stat) {
        log.debug("Request to save Stat : {}", stat);
        return statRepository.save(stat);
    }

    /**
     *  Get all the stats.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Stat> findAll(Pageable pageable) {
        log.debug("Request to get all Stats");
        return statRepository.findAll(pageable);
    }

    /**
     *  Get one stat by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<Stat> findOne(String id) {
        log.debug("Request to get Stat : {}", id);
        return statRepository.findById(id);
    }

    /**
     *  Delete the  stat by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Stat : {}", id);
        statRepository.deleteById(id);
    }
}
