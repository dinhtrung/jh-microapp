package com.ft.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ft.domain.Cdr;
import com.ft.domain.CdrStat;

public interface CdrStatCustomRepository {

	List<CdrStat> dashboardStats(Cdr searchModel);

	Page<CdrStat> findAll(Query query, Pageable pageable);

	Page<CdrStat> findAll(String query, Pageable pageable);

	Criteria createCriteria(CdrStat searchModel);

	Query createQuery(CdrStat searchModel);

	List<Cdr> search(CdrStat searchModel);

}
