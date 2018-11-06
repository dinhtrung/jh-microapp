package com.ft.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.DataFile;
import com.ft.domain.Product;
import com.ft.domain.Subscriber;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
 * Service Implementation for managing Subscriber.
 */
@Service
public class SubMsisdnService {

    private final Logger log = LoggerFactory.getLogger(SubMsisdnService.class);

    private final SubscriberRepository subMsisdnRepository;
    private final SubProductRepository subProductRepository;
    private final SmsLogRepository smsRepository;
    private final ObjectMapper mapper;

    public SubMsisdnService(SubscriberRepository subMsisdnRepository, SubProductRepository subProductRepository, SmsLogRepository smsRepository, ObjectMapper mapper) {
        this.subMsisdnRepository = subMsisdnRepository;
        this.subProductRepository = subProductRepository;
        this.smsRepository = smsRepository;
        this.mapper = mapper;
    }

    /**
     * Save a subMsisdn.
     *
     * @param subMsisdn the entity to save
     * @return the persisted entity
     */
    public Subscriber save(Subscriber subMsisdn) {
        log.debug("Request to save SubMsisdn : {}", subMsisdn);
        return subMsisdnRepository.save(subMsisdn);
    }

    /**
     * Get all the subMsisdns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Subscriber> findAll(Pageable pageable) {
        log.debug("Request to get all SubMsisdns");
        return subMsisdnRepository.findAll(pageable);
    }

    /**
     * @param searchModel JSON encoded values of Subscriber search Model
     * @param pageable
     * @return
     */
    public Page<Subscriber> findAll(String searchModel, Pageable pageable) {
        return (searchModel != null) ? subMsisdnRepository.findAll(searchModel, pageable) : findAll(pageable);
    }

    /**
     * Get all the subMsisdns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Subscriber> findAllAwaitNotify(Pageable pageable) {
        return subMsisdnRepository.findAllByNotifyBetweenAndStatusBetweenAndChargeNextTimeLessThanOrderByStatusDesc(Subscriber.NOTIFY_EXPIRED, Subscriber.NOTIFY_PROCESSED, Subscriber.STATUS_EXPIRED, 3//SubMsisdn.STATUS_LEFT
                , ZonedDateTime.now().plusDays(1), pageable);
    }

    /**
     * @param searchModel JSON encoded values of Subscriber search Model
     * @param pageable
     * @return
     */
    public Page<Subscriber> findAll(Subscriber searchModel, Pageable pageable) {
        return (searchModel != null) ? subMsisdnRepository.findAll(subMsisdnRepository.createQuery(searchModel), pageable) : findAll(pageable);
    }

    /**
     * Get all the subMsisdns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<Subscriber> findAllActive(Pageable pageable) {
        log.debug("Request to get all SubMsisdns");
        return subMsisdnRepository.findAllByStatusLessThanAndExpiryTimeGreaterThan(Subscriber.STATUS_LEFT, ZonedDateTime.now(), pageable);
    }

    /**
     * Get all the subMsisdns.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public List<Subscriber> findAllActive() {
        return subMsisdnRepository.findAllByStatusLessThanAndExpiryTimeGreaterThan(Subscriber.STATUS_LEFT, ZonedDateTime.now());
    }

    /**
     * Get one subMsisdn by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<Subscriber> findOne(String id) {
        log.debug("Request to get SubMsisdn : {}", id);
        return subMsisdnRepository.findById(id);
    }

    /**
     * Delete the subMsisdn by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete SubMsisdn : {}", id);
        subMsisdnRepository.deleteById(id);
    }

    public int importData(DataFile dataFile) {
        int result = 0;
        Optional<Product> productOpt = subProductRepository.findById(dataFile.getId());
        if (!productOpt.isPresent()) return 0;
        Product product = productOpt.get();
        List<Subscriber> records = new ArrayList<Subscriber>();
        if (dataFile.getDataFileContentType().contains("text")) {
            String[] msgList = StringUtils.split(new String(dataFile.getDataFile()), "\n");
            for (String msisdn : msgList) {
                try {
                    // FIXME: Add new MSISDN configuration here...
                    Subscriber record = new Subscriber()
                            .msisdn(msisdn)
                            .status(0)
                            .joinChannel("DATA-FILE")
                            .joinAt(ZonedDateTime.now())
                            .chargeNextTime(ZonedDateTime.now())
                            .productId(product.getId());
                    records.add(record);
                    result++;
                } catch (Exception e) {
                    log.error("Cannot import MSISDN: " + msisdn);
                }
            }
        }
        subMsisdnRepository.saveAll(records);
        return result;
    }

    public DataFile exportData(String id) {
        List<Subscriber> sub = subMsisdnRepository.findAllByStatusLessThanAndProductId(Subscriber.STATUS_LEFT, id);
        try {
            DataFile result = new DataFile();
            result.setDataFileContentType("application/json");
            result.setDataFile(mapper.writeValueAsBytes(sub));
            return result;
        } catch (JsonProcessingException e) {
            log.error("Cannot write value for products");
            e.printStackTrace();
        }
        return null;
    }

    public DataFile exportData() {
        List<Subscriber> sub = subMsisdnRepository.findAll();
        try {
            DataFile result = new DataFile();
            result.setDataFileContentType("application/json");
            result.setDataFile(mapper.writeValueAsBytes(sub));
            return result;
        } catch (JsonProcessingException e) {
            log.error("Cannot write value for products");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * New subscription
     */
    public Subscriber reg(String msisdn, String productId) {
        Optional<Product> prodOpt = subProductRepository.findById(productId);
        if (!prodOpt.isPresent())return null;
        Product prod = prodOpt.get();
        Subscriber sub = subMsisdnRepository.findOneByProductIdAndMsisdn(productId, msisdn);
        if (sub == null) {
            sub = new Subscriber().msisdn(msisdn).productId(productId);
        }
        return reg(sub, prod);
    }

    /**
     * New subscription
     */
    public Subscriber reg(String msisdn, Product prod) {
        if (prod == null) {
            return null;
        }
        Subscriber sub = subMsisdnRepository.findOneByProductIdAndMsisdn(prod.getId(), msisdn);
        if (sub == null) {
            sub = new Subscriber().msisdn(msisdn).productId(prod.getId());
        }
        return reg(sub, prod);
    }

    public Subscriber reg(Subscriber sub, Product prod) {
        return reg(sub, prod, null);
    }

    /**
     * Fill in all new subscription information
     */
    public Subscriber reg(Subscriber sub, Product product, String joinChannel) {
        sub.productId(product.getId())
                .joinAt(ZonedDateTime.now())
                .joinChannel(joinChannel)
                .notify(Subscriber.NOTIFY_DONE)
                .chargeNextTime(ZonedDateTime.now())
                .notifyLastTime(ZonedDateTime.now())
                .status(Subscriber.STATUS_PENDING);
        log.debug("REGISTER: sub " + sub + " --> product " + product);
        return save(sub);
    }

    /**
     * Un-subscription
     */
    public Subscriber unreg(String msisdn, String product) {
        Subscriber sub = subMsisdnRepository.findOneByProductIdAndMsisdn(product, msisdn);
        return unreg(sub);
    }

    public Subscriber unreg(Subscriber sub) {
        return unreg(sub, null);
    }

    public Subscriber unreg(Subscriber sub, String leftChannel) {
        if (sub == null) {
            return null;
        }
        sub
                .leftAt(ZonedDateTime.now())
                .leftChannel(leftChannel)
                .notify(Subscriber.NOTIFY_DONE)
                .expiryTime(ZonedDateTime.now())
                .status(Subscriber.STATUS_LEFT);
        return subMsisdnRepository.save(sub);
    }


    public void changeMsisdn(String msisdn, String newMSISDN, String timeStamp) {
        if (subMsisdnRepository.changeMsisdn(msisdn, newMSISDN) > 0) {
            log.debug("Successfully change MSISDN from " + msisdn + " --> " + newMSISDN);
            smsRepository.updatePendingSms(msisdn, newMSISDN, timeStamp);
        }
    }

    /**
     * Remove MSISDN unable to charge after 90 days
     *
     * @return
     */
    public Integer cleanup() {
        return subMsisdnRepository.deleteAllByChargeLastSuccessIsNullOrChargeLastSuccessLessThan(ZonedDateTime.now().minusDays(90));
    }

    public Subscriber findOneByMsisdnAndProduct(String msisdn, String pid) {
        return subMsisdnRepository.findOneByMsisdnAndProductId(msisdn, pid);
    }
}
