package com.ft.repository.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.fields;
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
import com.ft.domain.SmsLog;
import com.ft.repository.SmsLogCustomRepository;
import com.ft.service.dto.DashboardDTO;
import com.mongodb.client.result.UpdateResult;

public class SmsLogRepositoryImpl implements SmsLogCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ObjectMapper mapper;

	@Override
	public long updatePendingSms(String msisdn, String newMSISDN, String timestamp) {
		UpdateResult result = mongoTemplate.updateMulti(
				new Query(Criteria.where("destination").is(msisdn)
						.where("status").is(0)
						.where("submitAt").gt(timestamp)
						.where("submitAt").gt(ZonedDateTime.now())),
				Update.update("destination", newMSISDN), SmsLog.class);
		return result.getModifiedCount();
	}

	/**
	 * Reset status back to 0
	 */
	@Override
	public long resetStatus() {
		// TODO Auto-generated method stub
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("status").lt(2)), Update.update("status", 0),
				SmsLog.class);
		return result.getModifiedCount();
	}

	/**
	 * Statistical query
	 *
	 * @return
	 */
	@Override
	public List<SmsLog> stats() {
//		Aggregation agg = newAggregation(match(Criteria.where("status").lt(2)), group("productId").count().as("status"),
//				project("status"), sort(Sort.Direction.DESC, "status"));
//		AggregationResults<SmsLog> groupResults = mongoTemplate.aggregate(agg, SmsLog.class, SmsLog.class);
//		List<SmsLog> result = groupResults.getMappedResults();
//
//		return result;
		Aggregation agg = newAggregation(
				match(Criteria.where("status").is(1)),
			    project()
			    	.andExpression("dateToString('%Y-%m-%d', submitAt)").as("date")
			    	.andInclude("productId")
//			        .andExpression("concat(year(submitAt), month(submitAt), dayOfMonth(submitAt))").as("id")
//			        .andExpression("month(submitAt)").as("month")
//			        .andExpression("dayOfMonth(submitAt)").as("day")
//			        .andExpression("concat(year, month, day)").as("id")
			        ,
			    project().andExpression("concat(date, '-', productId)").as("id"),
			    group("id")
			        .count().as("status"),
//			    project("id", "productId", "status"),
			    sort(Sort.Direction.DESC, "id")
			);
		AggregationResults<SmsLog> groupResults = mongoTemplate.aggregate(agg, SmsLog.class, SmsLog.class);
		List<SmsLog> result = groupResults.getMappedResults();

		return result;
	}

	/**
	 * Statistical query
	 * @return
	 */
	@Override
	public List<SmsLog> search(SmsLog searchModel) {
		return mongoTemplate.find(createQuery(searchModel), SmsLog.class);
	}
	/**
	 * Provide search functionality for SMS
	 * @param searchModel
	 * @return
	 */
	@Override
	public Criteria createCriteria(SmsLog searchModel){
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null){
			if (searchModel.getStatus() != null) criteria.add(Criteria.where("status").is(searchModel.getStatus()));
			if (searchModel.getDestination() != null) criteria.add(Criteria.where("destination").is(searchModel.getDestination()));
			if (searchModel.getProductId() != null) criteria.add(Criteria.where("productId").is(searchModel.getProductId()));
			if (searchModel.getContentId() != null) criteria.add(Criteria.where("contentId").is(searchModel.getContentId()));
			if (searchModel.getSubmitAt() != null) criteria.add(Criteria.where("submitAt").gte(searchModel.getSubmitAt()));
			if (searchModel.getDeliveredAt() != null) criteria.add(Criteria.where("submitAt").lte(searchModel.getDeliveredAt()));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();
	}

	@Override
	public Query createQuery(SmsLog searchModel){
		return new Query().addCriteria(createCriteria(searchModel));
	}

	/**
	 * Find All entities based on Query
	 */
	@Override
	public Page<SmsLog> findAll(Query query, Pageable pageable){
		return new PageImpl<SmsLog>(mongoTemplate.find(query.with(pageable), SmsLog.class), pageable, mongoTemplate.count(query, SmsLog.class));
	}

	@Override
	public Page<SmsLog> findAll(String query, Pageable pageable){
		try {
			SmsLog searchModel = mapper.readValue(query, SmsLog.class);
			return findAll(createQuery(searchModel), pageable);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Statistical query
	 *
	 * @return
	 */
	@Override
	public List<DashboardDTO> stats(SmsLog searchModel) {
		Aggregation agg = newAggregation(
				match(createCriteria(searchModel)),
			    project()
			    	.andExpression("dateToString('%Y-%m-%d', submitAt)").as("date")
			    	.andInclude("productId")
				    ,group("date", "productId")
				        .count().as("cnt")
			        ,sort(Sort.Direction.DESC, "date")
			        ,sort(Sort.Direction.DESC, "productId")


			);
		AggregationResults<DashboardDTO> groupResults = mongoTemplate.aggregate(agg, SmsLog.class, DashboardDTO.class);
		List<DashboardDTO> result = groupResults.getMappedResults();

		return result;
	}
}
