package com.ft.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.DataFile;
import com.ft.domain.Product;
import com.ft.domain.SmsContent;
import com.ft.domain.SmsLog;
import com.ft.domain.Subscriber;
import com.ft.repository.SmsContentRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;
import com.ibm.icu.text.Transliterator;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.PageRequest;

/**
 * Service Implementation for managing SmsContent.
 */
@Service
public class SmsContentService {

    private final Logger log = LoggerFactory.getLogger(SmsContentService.class);

    private final SmsContentRepository smsContentRepository;

    private final SubProductRepository subProductRepository;

    private final SubscriberRepository msisdnRepo;

    private final ObjectMapper mapper;

    private final Transliterator transliterator;

    private final SmsLogRepository smsRepo;

    public SmsContentService(SmsContentRepository smsContentRepository, SubProductRepository subProductRepository,
            ObjectMapper mapper, SubscriberRepository msisdnRepo, SmsLogRepository smsRepo) {
        this.smsContentRepository = smsContentRepository;
        this.subProductRepository = subProductRepository;
        this.smsRepo = smsRepo;
        this.mapper = mapper;
        this.msisdnRepo = msisdnRepo;
        this.transliterator = Transliterator.getInstance("Any-Latin; NFD; [:M:] Remove; NFC; [^\\p{ASCII}] Remove");
    }

    /**
     * Save a smsContent.
     *
     * @param smsContent the entity to save
     * @return the persisted entity
     */
    public SmsContent save(SmsContent smsContent) {
        //log.debug("Request to save SmsContent : {}", smsContent);
        return smsContentRepository.save(smsContent);
    }

    /**
     * Get all the smsContents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    public Page<SmsContent> findAll(Pageable pageable) {
        //log.debug("Request to get all SmsContents");
        return smsContentRepository.findAll(pageable);
    }

    /**
     * @param searchModel JSON encoded values of SmsContent search Model
     * @param pageable
     * @return
     */
    public Page<SmsContent> findAll(String searchModel, Pageable pageable) {
        return (searchModel != null) ? smsContentRepository.findAll(searchModel, pageable) : findAll(pageable);
    }

    /**
     * @param searchModel JSON encoded values of SmsContent search Model
     * @param pageable
     * @return
     */
    public Page<SmsContent> findAll(SmsContent searchModel, Pageable pageable) {
        return (searchModel != null) ? smsContentRepository.findAll(smsContentRepository.createQuery(searchModel), pageable) : findAll(pageable);
    }

    /**
     * Get one smsContent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<SmsContent> findOne(String id) {
        //log.debug("Request to get SmsContent : {}", id);
        return smsContentRepository.findById(id);
    }

    /**
     * Delete the smsContent by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        //log.debug("Request to delete SmsContent : {}", id);
        smsContentRepository.deleteById(id);
    }

    public List<SmsContent> getToday() {
        return smsContentRepository.findAllByStatusAndStartAtBetween(SmsContent.STATE_ENABLE,
                ZonedDateTime.now().withMinute(0).withHour(0),
                ZonedDateTime.now().plusDays(1).withMinute(0).withHour(0));
    }

    public List<SmsContent> getTodayContents() {
        LocalDate localDate1 = LocalDate.now();
        Instant dayStart = localDate1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateStart = Date.from(dayStart);
        LocalDate localDate2 = LocalDate.now().plusDays(1);
        Instant dayEnd = localDate2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateEnd = Date.from(dayEnd);
        return smsContentRepository.findAllByStatusAndStartAtBetween(SmsContent.STATE_ENABLE,
                dateStart, dateEnd);
    }

    public List<SmsContent> getTodayContents(Product product) {
        // end of yesterday is the begining of today
        // begining of tommorrow is the end of today
        // crazy date, oh crazy 
        LocalDate localDate1 = LocalDate.now();
        Instant dayStart = localDate1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateStart = Date.from(dayStart);
        LocalDate localDate2 = LocalDate.now().plusDays(1);
        Instant dayEnd = localDate2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dateEnd = Date.from(dayEnd);
        List<SmsContent> found;
        int contentsPerDay = product.getContentPerDay();
        List<SmsContent> contents = smsContentRepository.findAllByStatusAndProductIdAndStartAtBetween(
                SmsContent.STATE_ENABLE, product.getId(), dateStart, dateEnd);
        if ((contents == null || contents.isEmpty() || contents.size() < contentsPerDay)
                && product.getContentType().equalsIgnoreCase("ROTATIONAL")) {
            try {
                // provision content first
                int counter = 0;
                do {
                    found = smsContentRepository.findAllByProductIdOrderByStartAtAsc(
                            product.getId(), new PageRequest(0, product.getContentPerDay()));
                    for (SmsContent sms : found) {
                        sms.setStartAt(ZonedDateTime.now());
                        sms.setExpiredAt(ZonedDateTime.now().plusHours(6));
                        String msg = sms.getMessage();
                        if (msg.length() > 160) {
                            sms.setStatus(SmsContent.STATE_DISABLE);
                        } else {
                            sms.setStatus(SmsContent.STATE_ENABLE);
                            counter = counter + 1;
                        }
                        smsContentRepository.save(sms);
                        if (counter >= contentsPerDay) {
                            break;
                        }
                    }
                } while (counter < contentsPerDay);
                log.info("Contents absent for a ROTATIONAL PRODUCT " + product.getCode()
                        + ". " + found.size() + " contents recycled. ");
                contents = smsContentRepository.findAllByStatusAndProductIdAndStartAtBetween(
                        SmsContent.STATE_ENABLE, product.getId(), dateStart, dateEnd);
                log.info("Recycling worked. Total:  " + contents.size());
            } catch (Exception e) {
            }
        }
        return contents;
    }

    public List<SmsContent> getToday(String productId) {
        log.debug(productId + "Start At: " + ZonedDateTime.now().withMinute(0).withHour(0) + " ---> "
                + ZonedDateTime.now().plusDays(1).withMinute(0).withHour(0));
        return smsContentRepository.findAllByStatusAndProductIdAndStartAtBetween(SmsContent.STATE_ENABLE, productId,
                ZonedDateTime.now().withMinute(0).withHour(0),
                ZonedDateTime.now().plusDays(1).withMinute(0).withHour(0));
    }

    public List<SmsContent> getScheduledContent(String productId, ZonedDateTime start, ZonedDateTime end) {
        return smsContentRepository.findAllByStatusAndProductIdAndStartAtBetween(SmsContent.STATE_ENABLE, productId, start, end);
    }

    /**
     * Import message contents from plain text file.
     *
     * @param dataFile
     * @return
     */
    public int importData(DataFile dataFile) {
        // TODO Auto-generated method stub
        int result = 0;
        Optional<Product> productOpt = subProductRepository.findById(dataFile.getId());
        if (!productOpt.isPresent()) return 0;
        Product product = productOpt.get();
        String[] schedules = StringUtils.split(product.getBroadcastHours(), "|");
        List<String> weekdays = Arrays.asList(StringUtils.split(product.getBroadcastWeekday(), "|"));
        //log.debug("Schedule Length: " + schedules.length + " -- Weekday Length: " + weekdays.size());
        // FIXME: Consider checking on Weekday too

        if (dataFile.getDataFileContentType().contains("json")) {
            try {
                List<SmsContent> data = mapper.readValue(dataFile.getDataFile(), new TypeReference<List<SmsContent>>() {
                });
                for (SmsContent s : data) {
                    try {
                        smsContentRepository.saveAll(data);
                    } catch (Exception e) {
                        log.error("Cannot import data: " + s + " due to " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                log.error("Cannot parse JSON");
                e.printStackTrace();
            }
        } else if (dataFile.getDataFileContentType().contains("text")) {
            String[] msgList = StringUtils.split(new String(dataFile.getDataFile()), "\n");
            // Delete old messages
//			if (msgList.length > 0) {
//				Long deleted = smsContentRepository.deleteByProductIdAndStartAtGreaterThan(dataFile.getId(), dataFile.getCreatedAt());
//				log.debug("Delete " + deleted + " exists messages since " + ZonedDateTime.now() + " for service #"
//						+ product.getId() + " - " + product.getCode());
//
//			}
            int index = 0;
            int scheduleIndex = 0;
            List<SmsContent> records = new ArrayList<SmsContent>();

            while (scheduleIndex < msgList.length) {
                //log.debug("Schedule Index: " + scheduleIndex + " Index: " + index);
                try {
                    ZonedDateTime startAt = calculateSchedule(dataFile.getCreatedAt(), index / schedules.length,
                            schedules[index % schedules.length]);
                    //log.debug("Start At: " + startAt + " weekday value: " + startAt.getDayOfWeek().getValue());
                    if (!weekdays.contains(startAt.getDayOfWeek().getValue() + "")) {
                        //log.debug("Skipped..." + index);
                        continue;
                    }
                    // FIXME: Expired Time should should be applied system-wide
                    ZonedDateTime expiredAt = calculateSchedule(dataFile.getCreatedAt(), index / schedules.length, "20:00:00");
                    // Create new content records
                    SmsContent d = new SmsContent();
                    // FIXME: More validation rules for message content
                    d.setMessage(msgList[scheduleIndex]);
                    d.setProductId(product.getId());
                    d.setStartAt(startAt);
                    d.setExpiredAt(expiredAt);
                    d.setStatus(SmsContent.STATE_ENABLE);
                    //log.debug("Save message: " + d);
                    records.add(d);
                    result++;
                } catch (Exception e) {
                    log.error("Missed line: " + index + " : " + e.getMessage());
                } finally {
                    scheduleIndex++;
                    index++;
                }

            }
            smsContentRepository.saveAll(records);
        } else {
            // Try parse using excel files
            ByteArrayInputStream bs = new ByteArrayInputStream(dataFile.getDataFile());
            ArrayList<String> msgList = new ArrayList<String>();
            try {
                Workbook workbook = WorkbookFactory.create(bs);
                Iterator<Sheet> sheetIterator = workbook.sheetIterator();
                // Loop for all sheet, extract the first column and create
                // report to import it
                while (sheetIterator.hasNext()) {
                    Sheet datatypeSheet = sheetIterator.next();
                    Iterator<Row> iterator = datatypeSheet.iterator();
                    while (iterator.hasNext()) {
                        Row currentRow = iterator.next();
                        Cell firstCell = currentRow.getCell(0);
                        if (firstCell == null) {
                            continue;
                        }
                        firstCell.setCellType(CellType.STRING);
                        String msg = transliterator.transliterate(firstCell.getStringCellValue()).trim();
                        if (!msg.equalsIgnoreCase("")) {
                            msgList.add(msg);
                        }
                    }
                }
                workbook.close();
//				if (msgList.size() > 0) {
//					smsContentRepository.deleteByProductIdAndStartAtGreaterThan(product.getId(), dataFile.getCreatedAt());
//				}
                int index = 0;
                int scheduleIndex = 0;
                List<SmsContent> records = new ArrayList<SmsContent>();

                while (scheduleIndex < msgList.size()) {
                    //log.debug("Schedule Index: " + scheduleIndex + " Index: " + index);
                    try {
                        ZonedDateTime startAt = calculateSchedule(dataFile.getCreatedAt(), index / schedules.length,
                                schedules[index % schedules.length]);
                        //log.debug("Start At: " + startAt + " weekday value: " + startAt.getDayOfWeek().getValue());
                        if (!weekdays.contains(startAt.getDayOfWeek().getValue() + "")) {
                            //log.debug("Skipped..." + index);
                            continue;
                        }
                        // FIXME: Expired Time should should be applied
                        // system-wide
                        ZonedDateTime expiredAt = calculateSchedule(dataFile.getCreatedAt(), index / schedules.length, "20:00:00");
                        // Create new content records
                        SmsContent d = new SmsContent();
                        // FIXME: More validation rules for message content
                        d.setMessage(msgList.get(scheduleIndex));
                        d.setProductId(product.getId());
                        d.setStartAt(startAt);
                        d.setExpiredAt(expiredAt);
                        d.setStatus(SmsContent.STATE_ENABLE);
                        //log.debug("Save message: " + d);
                        records.add(d);
                        result++;
                    } catch (Exception e) {
                        log.error("Missed line: " + index + " : " + e.getMessage());
                    } finally {
                        scheduleIndex++;
                        index++;
                    }
                }
                smsContentRepository.saveAll(records);
            } catch (EncryptedDocumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Calculate the next occur of the schedule
     *
     * @param nextDay The date offset from today
     * @param schedule The current hour that happens
     * @return
     * @throws Exception
     */
    private ZonedDateTime calculateSchedule(ZonedDateTime since, int nextDay, String schedule) throws Exception {
        return since
                .plusDays(nextDay)
                .withHour(Integer.parseInt(schedule.substring(0, 2)))
                .withMinute(Integer.parseInt(schedule.substring(3, 5)));
    }

    /**
     * Export list of messages
     *
     * @param id
     * @return
     */
    public DataFile exportData() {
        List<SmsContent> products = smsContentRepository.findAll();
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

    public Page<SmsContent> getProductContent(String id, Pageable pageable) {
        return smsContentRepository.findAllByProductId(id, pageable);
    }

    /**
     * Submit whole SMS Content for Today
     */
    public int submitToday() {
        int result = 0;
        for (SmsContent msg : getToday()) {
            //log.debug("Got message: " + msg);
            result += prepareSms(msg);
            msg.setStatus(SmsContent.STATE_DONE); // Processed
            smsContentRepository.save(msg);
        }
        return result;
    }

    /**
     * This method only for re-send SMS for whole SMS Content
     */
    public Integer prepareSms(SmsContent msg) {
        Optional<Product> productOpt = subProductRepository.findById(msg.getProductId());
        if (!productOpt.isPresent()) return null;
        Product product = productOpt.get();
        List<Subscriber> msisdnList = msisdnRepo.findAllByStatusLessThanAndProductId(Subscriber.STATUS_LEFT,
                msg.getProductId());
        int cnt = 0;
        for (Subscriber msisdn : msisdnList) {
            SmsLog sms = smsRepo.findOneByDestinationAndContentId(msisdn.getMsisdn(), msg.getId());
            if (sms == null) {
                sms = new SmsLog();
            }
            sms.productId(msg.getProductId()).contentId(msg.getId()).source(product.getBroadcastShortcode())
                    .destination(msisdn.getMsisdn()).text(msg.getMessage()).submitAt(ZonedDateTime.now())
                    .status(SmsLog.STATE_PENDING);
            smsRepo.save(sms);
            // After save the SMS, we must update the customer as well
        }
        return cnt;
    }
}
