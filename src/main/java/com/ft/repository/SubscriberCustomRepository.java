package com.ft.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ft.domain.Subscriber;
import com.ft.service.dto.DashboardDTO;

/**
 * Spring Data MongoDB repository for the Subscriber entity.
 */
@Repository
public interface SubscriberCustomRepository {

	long changeMsisdn(String msisdn, String newMSISDN);

	long resetActiveSubscriberStatus();

	List<Subscriber> stats();

	Criteria createCriteria(Subscriber searchModel);

	Query createQuery(Subscriber searchModel);

	Page<Subscriber> findAll(Query query, Pageable pageable);

	Page<Subscriber> findAll(String query, Pageable pageable);

	List<Subscriber> statJoined(Subscriber searchModel);

	List<Subscriber> search(Subscriber searchModel);

	long updateFailedStatus(String msisdn, int status);

	long updateSuccessStatus(String msisdn);

	List<DashboardDTO> dashboardJoinSub(ZonedDateTime from, ZonedDateTime to);

	List<DashboardDTO> dashboardLeftSub(ZonedDateTime from, ZonedDateTime to);
}
