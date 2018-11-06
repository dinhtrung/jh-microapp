package com.ft.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.ft.domain.Subscription;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Apr 3, 2018, 2:54:28 PM
 * @Quote To code is human, to debug is coffee
 *
 */
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    List<Subscription> findAllBySubscriberId(String subscriberId);

    long countByProductId(String id);

    List<Subscription> findAllByProductId(String id);

    List<Subscription> findAllByProductIdEqualsAndStatusEquals(String id, String status);

    List<Subscription> findAllBySubscriberIdAndProductId(String subscriberId, String productId);

}
