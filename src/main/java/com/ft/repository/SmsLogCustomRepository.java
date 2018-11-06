package com.ft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ft.domain.SmsLog;
import com.ft.service.dto.DashboardDTO;

public interface SmsLogCustomRepository {

	long resetStatus();

	List<SmsLog> stats();

	long updatePendingSms(String msisdn, String newMSISDN, String timestamp);

	List<SmsLog> search(SmsLog searchModel);

	Query createQuery(SmsLog searchModel);

	Page<SmsLog> findAll(Query createQuery, Pageable pageable);

	Page<SmsLog> findAll(String query, Pageable pageable);

	List<DashboardDTO> stats(SmsLog searchModel);

	Criteria createCriteria(SmsLog searchModel);

}
