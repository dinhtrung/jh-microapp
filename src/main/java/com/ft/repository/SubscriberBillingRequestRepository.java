package com.ft.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ft.domain.SubscriberBillingRequest;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Nov 25, 2017, 10:34:22 AM
 * @Quote To code is human, to debug is coffee
 *
 */
@Repository
public interface SubscriberBillingRequestRepository extends MongoRepository<SubscriberBillingRequest, String>{

}
