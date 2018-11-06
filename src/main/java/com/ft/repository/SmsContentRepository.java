package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.SmsContent;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the SmsContent entity.
 */
@Repository
public interface SmsContentRepository extends MongoRepository<SmsContent,String>, SmsContentCustomRepository {

	List<SmsContent> findAllByProductId(String id);

	Long deleteByProductIdAndStartAtGreaterThan(String id, ZonedDateTime now);
        
        List<SmsContent> findAllByProductIdOrderByStartAtAsc(String id, Pageable pageable);

	List<SmsContent> findAllByProductIdAndStartAtGreaterThanAndExpiredAtLessThan(String id, ZonedDateTime earlyToday,
			ZonedDateTime lateToday);

	List<SmsContent> findAllByProductIdAndStatusAndStartAtGreaterThanAndExpiredAtLessThan(String id, int stateEnable,
			ZonedDateTime now, ZonedDateTime now2);
        
        List<SmsContent> findAllByStatusAndStartAtBetween(int stateEnable, Date startDate, Date endDate);
        
        List<SmsContent> findAllByStatusAndProductIdAndStartAtBetween(int stateEnable, String productId,
			Date startDate, Date endDate);
	
	List<SmsContent> findAllByStatusAndStartAtBetween(int stateEnable, ZonedDateTime now, ZonedDateTime now2);

	List<SmsContent> findAllByStatusAndProductIdAndStartAtBetween(int stateEnable, String productId,
			ZonedDateTime withHour, ZonedDateTime withHour2);

	Page<SmsContent> findAllByProductId(String id, Pageable pageable);

}
