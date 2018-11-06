package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.SmsLog;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the SmsLog entity.
 */
@Repository
public interface SmsLogRepository extends MongoRepository<SmsLog, String>, SmsLogCustomRepository {

	List<SmsLog> findAllByStatusLessThanAndSubmitAtLessThan(int stateSubmitted, ZonedDateTime time);

	SmsLog findOneByMessageId(String messageId);

	int deleteByDestinationAndSubmitAtGreaterThan(String msisdn, ZonedDateTime now);

	SmsLog findOneByDestinationAndContentId(String msisdn, String id);

	int deleteByDestinationAndStatusLessThanAndProductId(String msisdn, int statePending, String productId);

	List<SmsLog> findAllByStatusBetweenAndSubmitAtLessThan(int stateSubmitted, int stateFailed, ZonedDateTime submitTime);

	List<SmsLog> findAllByContentIdAndStatusBetweenAndSubmitAtLessThan(String id, int stateFailed, int stateSubmitted,
			ZonedDateTime now);

}
