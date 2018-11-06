package com.ft.repository.impl;

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
import com.ft.domain.SmsContent;
import com.ft.repository.SmsContentCustomRepository;
import com.mongodb.client.result.UpdateResult;

// Import as static
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SmsContentRepositoryImpl implements SmsContentCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ObjectMapper mapper;
	
	/**
	 * Reset status back to 0
	 */
	@Override
	public long resetStatus() {
		// TODO Auto-generated method stub
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("status").lt(2)), Update.update("status", 0), SmsContent.class);
		return result.getModifiedCount();
	}

	/**
	 * Statistical query
	 * @return
	 */
	@Override
	public List<SmsContent> stats() {
		Aggregation agg = newAggregation(
				match(Criteria.where("status").is(SmsContent.STATE_ENABLE)
						.andOperator( Criteria.where("start_at").gt(ZonedDateTime.now()) )
				),
				group("productId").count().as("status"),
				project("status"),
				sort(Sort.Direction.DESC, "status")
			);
		AggregationResults<SmsContent> groupResults
		= mongoTemplate.aggregate(agg, SmsContent.class, SmsContent.class);
		List<SmsContent> result = groupResults.getMappedResults();

		return result;
	}
	/**
	 * Statistical query
	 * @return
	 */
	@Override
	public List<SmsContent> stats(SmsContent searchModel) {
		Aggregation agg = newAggregation(
				match(createCriteria(searchModel)),
				group("productId").count().as("status"),
				project("status"),
				sort(Sort.Direction.DESC, "status")
			);
		AggregationResults<SmsContent> groupResults
		= mongoTemplate.aggregate(agg, SmsContent.class, SmsContent.class);
		List<SmsContent> result = groupResults.getMappedResults();

		return result;
	}

	@Override
	public Criteria createCriteria(SmsContent searchModel) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null){
			if (searchModel.getStatus() != null) criteria.add(Criteria.where("status").is(searchModel.getStatus()));
			if (searchModel.getProductId() != null) criteria.add(Criteria.where("productId").is(searchModel.getProductId()));
			if (searchModel.getStartAt() != null) criteria.add(Criteria.where("startAt").gt(searchModel.getStartAt()));
			if (searchModel.getExpiredAt() != null) criteria.add(Criteria.where("startAt").lt(searchModel.getExpiredAt()));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();
	}

	@Override
	public Query createQuery(SmsContent searchModel) {
		return new Query().addCriteria(createCriteria(searchModel));
	}

	@Override
	public Page<SmsContent> findAll(String query, Pageable pageable) {
		try {
			SmsContent searchModel = mapper.readValue(query, SmsContent.class);
			return findAll(createQuery(searchModel), pageable);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public Page<SmsContent> findAll(Query query, Pageable pageable){
		return new PageImpl<SmsContent>(mongoTemplate.find(query.with(pageable), SmsContent.class), pageable, mongoTemplate.count(query, SmsContent.class));
	}

	@Override
	public List<SmsContent> search(SmsContent searchModel) {
		return mongoTemplate.find(createQuery(searchModel), SmsContent.class);
	}

	@Override
	public List<SmsContent> statsPending() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
