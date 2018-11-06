package com.ft.repository.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
// Import as static
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.time.ZonedDateTime;
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
import org.springframework.data.mongodb.core.query.Update;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Cdr;
import com.ft.domain.CdrStat;
import com.ft.repository.CdrCustomRepository;
import com.ft.security.AuthoritiesConstants;
import com.ft.security.SecurityUtils;
import com.mongodb.client.result.UpdateResult;

public class CdrRepositoryImpl implements CdrCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ObjectMapper mapper;

	/**
	 * Reset status back to 0
	 */
	@Override
	public long resetStatus() {
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("status").lt(2)), Update.update("status", 0), Cdr.class);
		return result.getModifiedCount();
	}

	@Override
	public List<Cdr> stats() {
		Aggregation agg = newAggregation(
			    project()
				    .andExpression("dateToString('%Y-%m-%dT%H', requestAt)").as("date")
				    .andInclude("amount")
		        	.andInclude("productId")
			    ,
			    group("date", "productId")
			    	.count().as("cnt")
			    	.sum("amount").as("revenue")
			    	.first("productId").as("ref")
			    	.first("date").as("date")
			    	,
		        sort(Sort.Direction.DESC, "date")
			);
		AggregationResults<Cdr> groupResults
		= mongoTemplate.aggregate(agg, Cdr.class, Cdr.class);
		List<Cdr> result = groupResults.getMappedResults();
		return result;
	}

	/**
	 * Statistical query
	 * @return
	 */
	@Override
	public List<Cdr> search(Cdr searchModel) {
		return mongoTemplate.find(createQuery(searchModel), Cdr.class);
	}
	/**
	 * Provide search functionality for SMS
	 * @param searchModel
	 * @return
	 */
	@Override
	public Criteria createCriteria(Cdr searchModel){
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null){
			if (searchModel.getMsisdn() != null) criteria.add(where("msisdn").is(searchModel.getMsisdn()));
			if (searchModel.getProductId() != null) criteria.add(Criteria.where("productId").is(searchModel.getProductId()));
			if (searchModel.getPartnerCode() != null) criteria.add(Criteria.where("partnerCode").is(searchModel.getPartnerCode()));
			if (searchModel.getRequestAt() != null) criteria.add(Criteria.where("requestAt").gte(searchModel.getRequestAt()));
			if (searchModel.getResponseAt() != null) criteria.add(Criteria.where("requestAt").lte(searchModel.getResponseAt()));
			if (searchModel.isStatus() != null) criteria.add(Criteria.where("status").is(searchModel.isStatus()));
		}
		// Search by user, too
		if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)) {
			// Try to search by his username
			criteria.add(Criteria.where("partnerCode").is(SecurityUtils.getCurrentUserLogin()));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();
	}

	@Override
	public Query createQuery(Cdr searchModel){
		return new Query().addCriteria(createCriteria(searchModel));
	}

	/**
	 * Find All entities based on Query
	 */
	@Override
	public Page<Cdr> findAll(Query query, Pageable pageable){
		return new PageImpl<Cdr>(mongoTemplate.find(query.with(pageable), Cdr.class), pageable, mongoTemplate.count(query, Cdr.class));
	}

	@Override
	public Page<Cdr> findAll(String query, Pageable pageable){
		try {
			Cdr searchModel = mapper.readValue(query, Cdr.class);
			return findAll(createQuery(searchModel), pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Statistical query
	 *
	 * @return
	 */
	@Override
	public List<Cdr> stats(Cdr searchModel) {
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
		// Search by user, too
		if (SecurityUtils.getCurrentUserLogin().isPresent() && SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)) {
			searchModel.setPartnerCode(SecurityUtils.getCurrentUserLogin().get());
		}
		Aggregation agg = newAggregation(
				match(createCriteria(searchModel)),
				project()
				    .andExpression("dateToString('%Y-%m-%d', requestAt)").as("date")
				    .andInclude("amount")
		        	.andInclude("productId")
			    ,
			    group("date", "productId")
			    	.count().as("cnt")
			    	.sum("amount").as("revenue")
			    	.first("productId").as("ref")
			    	.first("date").as("date")
			    	,
		        sort(Sort.Direction.DESC, "date")
			);
		AggregationResults<Cdr> groupResults = mongoTemplate.aggregate(agg, Cdr.class, Cdr.class);
		List<Cdr> result = groupResults.getMappedResults();
		return result;
	}

	@Override
	public List<Cdr> stats(String query){
		Cdr searchModel = new Cdr();
		try {
			searchModel = mapper.readValue(query, Cdr.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stats(searchModel);
	}

	@Override
	public List<CdrStat> getStats(Cdr searchModel) {
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
				project()
					.andExpression("dateToString('%Y-%m-%dT%H', requestAt)").as("dateTime")
				    .andInclude("amount")
		        	.andInclude("productId")
		        	.andExpression("concat('"+CdrStat.TYPE_PRODUCT+"')").as("ref_type")
		        	.andExpression("concat(productId, '_', dateToString('%Y-%m-%dT%H', requestAt))").as("id")
			    ,
			    group("dateTime", "productId")
			    	.count().as("cnt")
			    	.sum("amount").as("revenue")
			    	.first("productId").as("ref_id")
			    	.first("dateTime").as("date_time")
			    	.first("ref_type").as("ref_type")
			    	.first("id").as("id")
			    	,
			    sort(Sort.Direction.DESC, "date_time"),
		        sort(Sort.Direction.DESC, "ref_id")
//		        sort(Sort.Direction.DESC, "stat_hour")
			);
		AggregationResults<CdrStat> groupResults = mongoTemplate.aggregate(agg, Cdr.class, CdrStat.class);
		List<CdrStat> result = groupResults.getMappedResults();

		return result;
	}

	@Override
	public void removeFalseCdr() {
		Cdr searchModel = new Cdr().status(false);
		Query searchQuery = new Query(createCriteria(searchModel));
		mongoTemplate.remove(searchQuery, Cdr.class);
	}
}
