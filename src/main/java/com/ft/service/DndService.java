package com.ft.service;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;
import com.ft.domain.DataFile;
import com.ft.domain.Dnd;
import com.ft.domain.Subscriber;
import com.ft.repository.DndRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SubscriberRepository;


/**
 * Service Implementation for managing Dnd.
 */
@Service
public class DndService {

    private final Logger log = LoggerFactory.getLogger(DndService.class);

    private final DndRepository dndRepository;

    private final SubscriberRepository msisdnRepository;

    private final SmsLogRepository msgRepository;

    private final ApplicationProperties props;

    public DndService(DndRepository dndRepository, SubscriberRepository msisdnRepository, SmsLogRepository msgRepository, ApplicationProperties props) {
        this.dndRepository = dndRepository;
        this.msisdnRepository = msisdnRepository;
        this.msgRepository = msgRepository;
        this.props = props;
    }

    /**
     * Save a dnd.
     *
     * @param dnd the entity to save
     * @return the persisted entity
     */
    public Dnd save(Dnd dnd) {
        log.debug("Request to save Dnd : {}", dnd);
        msisdnRepository.updateFailedStatus(dnd.getId(), Subscriber.STATUS_LEFT);
        return dndRepository.save(dnd);
    }

    /**
     *  Get all the dnds.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Dnd> findAll(Pageable pageable) {
        log.debug("Request to get all Dnds");
        return dndRepository.findAll(pageable);
    }

    /**
     *  Get all the dnds.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Dnd> findAll(String query, Pageable pageable) {
        log.debug("Request to get all Dnds");
        return ((query == null) || query.trim().equalsIgnoreCase("")) ? dndRepository.findAll(pageable) : dndRepository.findAllByIdLike(query, pageable);
    }

    /**
     *  Get one dnd by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<Dnd> findOne(String id) {
        log.debug("Request to get Dnd : {}", id);
        return dndRepository.findById(id);
    }

    /**
     *  Delete the  dnd by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Dnd : {}", id);
        dndRepository.deleteById(id);
    }

    /**
     * Import data from file
     * @param dataFile
     * @return
     */
	public int importData(DataFile dataFile) {
		// TODO Auto-generated method stub
		log.debug("Got a file to process" + dataFile);
		int result = 0;
		if (dataFile.getDataFileContentType().contains("text")){
			String[] dndList = new String(dataFile.getDataFile()).split("\n");
			for (String msisdn: dndList){
				try {
					Dnd d = new Dnd();
					d.setJoinAt(ZonedDateTime.now());
					d.setId(props.msisdnFormat(msisdn));
					d.setJoinChannel("CUSTOMER-PORTAL");
					this.save(d);
					result ++;
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	/**
	 * Export data into CSV.
	 * FIXME: This isn't the best but we just stick with it first
	 * @return
	 */
	public DataFile exportData() {
		DataFile result = new DataFile();
		result.setDataFileContentType("text/csv");
		String rs = "";
		// Save all MSISDN into CSV
		for (Dnd dnd: dndRepository.findAll()){
			rs += dnd.getId() + "\n";
		}
		log.debug("File content: " + rs);
		result.setDataFile(rs.getBytes());
		return result;
	}
}
