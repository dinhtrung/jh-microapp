package com.ft.repository.impl;

// Import as static
import static org.springframework.data.mongodb.core.aggregation.Aggregation.fields;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
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
import com.ft.domain.Subscriber;
import com.ft.repository.SubscriberCustomRepository;
import com.ft.service.dto.DashboardDTO;
import com.mongodb.client.result.UpdateResult;

public class SubscriberRepositoryImpl implements SubscriberCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ObjectMapper mapper;

	@Override
	public long changeMsisdn(String msisdn, String newMSISDN) {
		// TODO Auto-generated method stub
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("msisdn").is(msisdn)),
				Update.update("msisdn", newMSISDN), Subscriber.class);
		return result.getModifiedCount();
	}

	/**
	 * When we found one MSISDN failed to charge, we ask it to wait for 3 more hours
	 * to try next time.
	 */
	@Override
	public long updateFailedStatus(String msisdn, int status) {
		// TODO Auto-generated method stub
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("status").lt(Subscriber.STATUS_PROCESSED).and("msisdn").is(msisdn)),
				Update.update("status", status).set("chargeNextTime", ZonedDateTime.now().plusHours(3))
						.set("chargeLastTime", ZonedDateTime.now()),
				Subscriber.class);
		return result.getModifiedCount();
	}

	/**
	 * When we found one MSISDN success to charge, we prioritize him on our queue
	 */
	@Override
	public long updateSuccessStatus(String msisdn) {
		// TODO Auto-generated method stub
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("status").lt(Subscriber.STATUS_PROCESSED).and("msisdn").is(msisdn)),
				Update.update("status", Subscriber.STATUS_PENDING).set("chargeNextTime",
						ZonedDateTime.now().withHour(1)),
				Subscriber.class);
		return result.getModifiedCount();
	}

	/**
	 * Reset charging status
	 */
	public long resetActiveSubscriberStatus() {
		// TODO Auto-generated method stub
		UpdateResult result = mongoTemplate.updateMulti(new Query(where("status").lte(Subscriber.STATUS_PROCESSED)),
				Update.update("status", Subscriber.STATUS_PENDING), Subscriber.class);
		return result.getModifiedCount();
	}

	@Override
	public List<Subscriber> stats() {
		Aggregation agg = newAggregation(match(Criteria.where("status").lt(2)), group("productId").count().as("status"),
				project("status"), sort(Sort.Direction.DESC, "status"));
		AggregationResults<Subscriber> groupResults = mongoTemplate.aggregate(agg, Subscriber.class, Subscriber.class);
		List<Subscriber> result = groupResults.getMappedResults();

		return result;
	}

	/**
	 * Statistical query
	 *
	 * @return
	 */
	@Override
	public List<Subscriber> search(Subscriber searchModel) {
		return mongoTemplate.find(createQuery(searchModel), Subscriber.class);
	}

	/**
	 * Provide search functionality for SMS
	 *
	 * @param searchModel
	 * @return
	 */
	@Override
	public Criteria createCriteria(Subscriber searchModel) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (searchModel != null) {
			if (searchModel.getStatus() != null)
				criteria.add(Criteria.where("status").is(searchModel.getStatus()));
			if (searchModel.getMsisdn() != null)
				criteria.add(Criteria.where("msisdn").is(searchModel.getMsisdn()));
			if (searchModel.getProductId() != null)
				criteria.add(Criteria.where("productId").is(searchModel.getProductId()));
			if (searchModel.getPartnerCode() != null)
				criteria.add(Criteria.where("partnerCode").is(searchModel.getPartnerCode()));
			if (searchModel.getJoinChannel() != null)
				criteria.add(Criteria.where("joinChannel").is(searchModel.getJoinChannel()));
			if (searchModel.getJoinAt() != null)
				criteria.add(Criteria.where("joinAt").gte(searchModel.getJoinAt()));
			if (searchModel.getLeftAt() != null)
				criteria.add(Criteria.where("leftAt").lte(searchModel.getLeftAt()));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]))
				: new Criteria();
	}

	@Override
	public Query createQuery(Subscriber searchModel) {
		return new Query().addCriteria(createCriteria(searchModel));
	}

	/**
	 * Find All entities based on Query
	 */
	@Override
	public Page<Subscriber> findAll(Query query, Pageable pageable) {
		return new PageImpl<Subscriber>(mongoTemplate.find(query.with(pageable), Subscriber.class), pageable,
				mongoTemplate.count(query, Subscriber.class));
	}

	@Override
	public Page<Subscriber> findAll(String query, Pageable pageable) {
		try {
			Subscriber searchModel = mapper.readValue(query, Subscriber.class);
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
	public List<Subscriber> statJoined(Subscriber searchModel) {
		Aggregation agg = newAggregation(match(createCriteria(searchModel)),
				project().andExpression("year(joinAt)").as("year").andExpression("month(joinAt)").as("month")
						.andExpression("dayOfMonth(joinAt)").as("day").andInclude("productId"),
				group(fields("submitAt").and("year").and("month").and("day").and("productId")).count().as("status")
						.sum("amount").as("amount"),
				sort(Sort.Direction.DESC, "id"));
		AggregationResults<Subscriber> groupResults = mongoTemplate.aggregate(agg, Subscriber.class, Subscriber.class);
		List<Subscriber> result = groupResults.getMappedResults();

		return result;
	}

	private Criteria createCriteriaDashboard(ZonedDateTime from, ZonedDateTime to, String field) {
		List<Criteria> criteria = new ArrayList<Criteria>();
		if (from != null) {
			criteria.add(Criteria.where(field).gte(from));
		}
		if (to != null) {
			criteria.add(Criteria.where(field).lte(to));
		}
		return (criteria.size() > 0) ? new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()]))
				: new Criteria();
	}

	private List<DashboardDTO> dashboardSub(ZonedDateTime from, ZonedDateTime to, String field) {
		Criteria criteria = createCriteriaDashboard(from, to, field);
		Aggregation agg = newAggregation(match(criteria),
				project().andExpression("dateToString('%Y-%m-%d', " + field + ")").as("date").andInclude("productId"),
				group("date", "productId").count().as("cnt"), sort(Sort.Direction.DESC, "date"),
				sort(Sort.Direction.DESC, "productId"));

		AggregationResults<DashboardDTO> groupResults = mongoTemplate.aggregate(agg, Subscriber.class,
				DashboardDTO.class);
		List<DashboardDTO> result = groupResults.getMappedResults();

		return result;
	}

	@Override
	public List<DashboardDTO> dashboardJoinSub(ZonedDateTime from, ZonedDateTime to) {
		return dashboardSub(from, to, "joinAt");
	}

	@Override
	public List<DashboardDTO> dashboardLeftSub(ZonedDateTime from, ZonedDateTime to) {
		return dashboardSub(from, to, "leftAt");
	}
}
