package com.ft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ft.domain.SmsContent;

public interface SmsContentCustomRepository {

	long resetStatus();

	List<SmsContent> stats(SmsContent searchModel);

	Criteria createCriteria(SmsContent searchModel);

	Query createQuery(SmsContent searchModel);

	Page<SmsContent> findAll(Query query, Pageable pageable);

	Page<SmsContent> findAll(String query, Pageable pageable);

	List<SmsContent> search(SmsContent searchModel);

	List<SmsContent> statsPending();

	List<SmsContent> stats();

}
