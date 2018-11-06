package com.ft.repository.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Cdr;
import com.ft.domain.CdrStat;
import com.ft.repository.CdrStatCustomRepository;

public class CdrStatRepositoryImpl implements CdrStatCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ObjectMapper mapper;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");



	public Criteria createCriteria(Cdr searchModel){
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null){
			if (searchModel.getRequestAt() != null) {
				criteria.add(Criteria.where("dateTime").gte(searchModel.getRequestAt().format(formatter)));
			}
			if (searchModel.getResponseAt() != null) {
				criteria.add(Criteria.where("dateTime").lte(searchModel.getResponseAt().format(formatter)));
			}
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();
	}

	@Override
	public List<CdrStat> dashboardStats(Cdr searchModel) {
		// Set default search filtering since beginning of this month
		if (searchModel == null){
			searchModel = new Cdr()
				.requestAt(
						ZonedDateTime.now()
						.withDayOfMonth(1)
						.withHour(0)
						.withMinute(0)
						.withSecond(0)
				);
		}
		Aggregation agg = newAggregation(
				match(createCriteria(searchModel)),
//				project()
//					.andInclude("dateTime")
//				    .andInclude("cnt")
//		        	.andInclude("revenue")
//		        	.andInclude("refId")
//		        	.andInclude("refType")
//		        	.andInclude("date")
//			    ,
			    group("dateTime", "refId", "refType")
			    	.sum("cnt").as("cnt")
			    	.sum("revenue").as("revenue")
			    	.first("refId").as("ref_id")
			    	.first("refType").as("ref_type")
			    	.first("dateTime").as("stat_date")
			    	.first("dateTime").as("date_time")
			    	,
			    	sort(Sort.Direction.DESC, "dateTime"),
			    	sort(Sort.Direction.DESC, "ref_id")

			);
		AggregationResults<CdrStat> groupResults = mongoTemplate.aggregate(agg, CdrStat.class, CdrStat.class);
		List<CdrStat> result = groupResults.getMappedResults();
		return result;
	}

	@Override
	public Page<CdrStat> findAll(Query query, Pageable pageable) {
		return new PageImpl<CdrStat>(mongoTemplate.find(query.with(pageable), CdrStat.class), pageable, mongoTemplate.count(query, CdrStat.class));
	}

	@Override
	public Page<CdrStat> findAll(String query, Pageable pageable) {
		try {
			CdrStat searchModel = mapper.readValue(query, CdrStat.class);
			return findAll(createQuery(searchModel), pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Statistical query
	 * @return
	 */
	@Override
	public List<Cdr> search(CdrStat searchModel) {
		return mongoTemplate.find(createQuery(searchModel), Cdr.class);
	}

	/**
	 * Provide search functionality for SMS
	 * @param searchModel
	 * @return
	 */
	@Override
	public Criteria createCriteria(CdrStat searchModel){
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null){
			if (searchModel.getDateTime() != null) criteria.add(where("dateTime").regex(searchModel.getDateTime()));
			if (searchModel.getDateFrom() != null) criteria.add(where("dateTime").gte(searchModel.getDateFrom().format(formatter)));
			if (searchModel.getDateTo() != null) criteria.add(where("dateTime").lte(searchModel.getDateTo().format(formatter)));
			if (searchModel.getRefId() != null) criteria.add(Criteria.where("refId").is(searchModel.getRefId()));
			if (searchModel.getRefType() != null) criteria.add(Criteria.where("refType").is(searchModel.getRefType()));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();
	}

	@Override
	public Query createQuery(CdrStat searchModel){
		return new Query().addCriteria(createCriteria(searchModel));
	}

}
