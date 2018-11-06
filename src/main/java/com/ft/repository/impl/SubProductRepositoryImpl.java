package com.ft.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Product;
import com.ft.repository.SubProductCustomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

// Import as static
import static org.springframework.data.mongodb.core.aggregation.Aggregation.fields;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class SubProductRepositoryImpl implements SubProductCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ObjectMapper mapper;
	
	@Override
	public List<Product> stats() {
		Aggregation agg = newAggregation(
				match(Criteria.where("status").lt(2)),
				group("productId").count().as("status"),
				project("status"),
				sort(Sort.Direction.DESC, "status")
			);
		AggregationResults<Product> groupResults
		= mongoTemplate.aggregate(agg, Product.class, Product.class);
		List<Product> result = groupResults.getMappedResults();

		return result;
	}
	
	/**
	 * Statistical query
	 * @return
	 */
	@Override
	public List<Product> search(Product searchModel) {
		return mongoTemplate.find(createQuery(searchModel), Product.class);
	}
	/**
	 * Provide search functionality for SMS
	 * @param searchModel
	 * @return
	 */
	@Override
	public Criteria createCriteria(Product searchModel){
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null){
			if (searchModel.isStatus() != null) criteria.add(Criteria.where("status").is(searchModel.isStatus()));
			if (searchModel.getCode() != null) criteria.add(Criteria.where("code").is(searchModel.getCode()));
			if (searchModel.getPartnerCode() != null) criteria.add(Criteria.where("partnerCode").is(searchModel.getPartnerCode()));
			if (searchModel.getJoinChannel() != null) criteria.add(Criteria.where("joinChannel").is(searchModel.getJoinChannel()));
			if (searchModel.getBroadcastShortcode() != null) criteria.add(Criteria.where("broadcastShortcode").gte(searchModel.getBroadcastShortcode()));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])) : new Criteria();
	}
	
	@Override
	public Query createQuery(Product searchModel){
		return new Query().addCriteria(createCriteria(searchModel));
	}
	
	/**
	 * Find All entities based on Query
	 */
	@Override
	public Page<Product> findAll(Query query, Pageable pageable){
		return new PageImpl<Product>(mongoTemplate.find(query.with(pageable), Product.class), pageable, mongoTemplate.count(query, Product.class));
	}
	
	@Override
	public Page<Product> findAll(String query, Pageable pageable){
		try {
			Product searchModel = mapper.readValue(query, Product.class);
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
	public List<Product> stats(Product searchModel) {
		Aggregation agg = newAggregation(
				match(createCriteria(searchModel)),
			    project()       
			        .andExpression("year(joinAt)").as("year")
			        .andExpression("month(joinAt)").as("month")
			        .andExpression("dayOfMonth(joinAt)").as("day")
			        .andInclude("productId")
			        ,
			    group(fields("submitAt").and("year").and("month").and("day").and("productId"))     
			        .count().as("status").sum("amount").as("amount"),
		        sort(Sort.Direction.DESC, "id")
			);
		AggregationResults<Product> groupResults = mongoTemplate.aggregate(agg, Product.class, Product.class);
		List<Product> result = groupResults.getMappedResults();

		return result;
	}
}
