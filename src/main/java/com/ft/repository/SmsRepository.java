package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Sms;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Sms entity.
 */
@Repository
public interface SmsRepository extends MongoRepository<Sms, String> {

	List<Sms> findAllByStatusLessThanAndSubmitAtLessThan(int stateSubmitted, ZonedDateTime now);

	Sms findOneByMessageId(String messageId);

	List<Sms> findAllByStatusBetweenAndSubmitAtLessThan(int stateFailed, int stateSubmitted, ZonedDateTime now);

}
