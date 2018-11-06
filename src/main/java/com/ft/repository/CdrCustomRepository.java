package com.ft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ft.domain.Cdr;
import com.ft.domain.CdrStat;

public interface CdrCustomRepository {

	long resetStatus();

	List<Cdr> stats();

	List<Cdr> search(Cdr searchModel);

	Criteria createCriteria(Cdr searchModel);

	Query createQuery(Cdr searchModel);

	Page<Cdr> findAll(Query query, Pageable pageable);

	Page<Cdr> findAll(String query, Pageable pageable);

	List<Cdr> stats(Cdr searchModel);

	List<Cdr> stats(String query);
	
	List<CdrStat> getStats(Cdr searchModel);

	void removeFalseCdr();
}
