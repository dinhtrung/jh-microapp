package com.ft.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ft.domain.DataFile;
import com.ft.repository.DataFileRepository;


/**
 * Service Implementation for managing DataFile.
 */
@Service
public class DataFileService {

    private final Logger log = LoggerFactory.getLogger(DataFileService.class);

    private final DataFileRepository dataFileRepository;
    public DataFileService(DataFileRepository dataFileRepository) {
        this.dataFileRepository = dataFileRepository;
    }

    /**
     * Save a dataFile.
     *
     * @param dataFile the entity to save
     * @return the persisted entity
     */
    public DataFile save(DataFile dataFile) {
        log.debug("Request to save DataFile : {}", dataFile);
        return dataFileRepository.save(dataFile);
    }

    /**
     *  Get all the dataFiles.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<DataFile> findAll(Pageable pageable) {
        log.debug("Request to get all DataFiles");
        return dataFileRepository.findAll(pageable);
    }

    /**
     *  Get one dataFile by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<DataFile> findOne(String id) {
        log.debug("Request to get DataFile : {}", id);
        return dataFileRepository.findById(id);
    }

    /**
     *  Delete the  dataFile by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete DataFile : {}", id);
        dataFileRepository.deleteById(id);
    }
}
