package com.ft.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.config.ApplicationProperties;
import com.ft.domain.DataFile;
import com.ft.domain.Product;
import com.ft.repository.SubProductRepository;
import com.ft.service.util.BeanUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing Product.
 */
@Service
public class SubProductService {

    private final Logger log = LoggerFactory.getLogger(SubProductService.class);

    private final SubProductRepository subProductRepository;

    private final ObjectMapper mapper;

    private final ApplicationProperties props;

    public SubProductService(SubProductRepository subProductRepository, ObjectMapper mapper, ApplicationProperties props) {
        this.subProductRepository = subProductRepository;
        this.mapper = mapper;
        this.props = props;
    }

    /**
     * Save a subProduct.
     *
     * @param subProduct the entity to save
     * @return the persisted entity
     */
    public Product save(Product subProduct) {
        log.debug("Request to save SubProduct : {}", subProduct);
        return subProductRepository.save(subProduct);
    }

    /**
     *  Get all the subProducts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all SubProducts");
        return subProductRepository.findAll(pageable);
    }

    /**
     *  Get all the subProducts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public List<Product> findAll() {
        return subProductRepository.findAll();
    }

    /**
     * @param searchModel JSON encoded values of Product search Model
     * @param pageable
     * @return
     */
	public Page<Product> findAll(String searchModel, Pageable pageable) {
		return (searchModel != null) ? subProductRepository.findAll(searchModel, pageable) : findAll(pageable) ;
	}

	/**
     * @param searchModel JSON encoded values of Product search Model
     * @param pageable
     * @return
     */
	public Page<Product> findAll(Product searchModel, Pageable pageable) {
		return (searchModel != null) ? subProductRepository.findAll(subProductRepository.createQuery(searchModel), pageable) : findAll(pageable) ;
	}

    /**
     *  Get one subProduct by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<Product> findOne(String id) {
        log.debug("Request to get SubProduct : {}", id);
        return subProductRepository.findById(id);
    }

    /**
     *  Delete the  subProduct by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete SubProduct : {}", id);
        subProductRepository.deleteById(id);
    }


	public int importData(DataFile dataFile) {
		if (dataFile.getDataFileContentType().contains("json")){
			try {
				List<Product> data = mapper.readValue(dataFile.getDataFile(), new TypeReference<List<Product>>() { });
				for (Product s: data){
					log.debug("Found product: " + s);
					try {
						subProductRepository.save(s);
					} catch (Exception e){
						log.error("Cannot import data: " + s + " due to " + e.getMessage());
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				log.error("Cannot parse JSON");
				e.printStackTrace();
			}
		}
		return 0;
	}

	public DataFile exportData() {
		List<Product> products = subProductRepository.findAll();
		try {
			DataFile result = new DataFile();
			result.setDataFileContentType("application/json");
			result.setDataFile(mapper.writeValueAsBytes(products));
			return result;
		} catch (JsonProcessingException e) {
			log.error("Cannot write value for products");
			e.printStackTrace();
		}
		return null;
	}
}
