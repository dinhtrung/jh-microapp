package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Subscriber;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Subscriber entity.
 */
@Repository
public interface SubscriberRepository extends MongoRepository<Subscriber, String>, SubscriberCustomRepository {

    Subscriber findOneByMsisdn(String id);

    List<Subscriber> findAllByProductId(String id);

    List<Subscriber> findAllByProductIdEqualsAndStatusEquals(String id, int status);

    Subscriber findOneByMsisdnAndProductId(String msisdn, String productID);

    long countByProductId(String id);

    List<Subscriber> findAllByStatusLessThan(int i, Pageable pageable);

    List<Subscriber> findAllByStatus(int i, Pageable pageable);

    Page<Subscriber> findAllByStatusLessThanAndExpiryTimeGreaterThan(int i, ZonedDateTime date, Pageable pageable);

    List<Subscriber> findAllByStatusLessThanAndExpiryTimeGreaterThan(int statusLeft, ZonedDateTime now);

    Page<Subscriber> findAllByStatusLessThanAndProductId(int i, String id, Pageable pageable);

    List<Subscriber> findAllByStatusLessThanAndProductId(int i, String id);

    Subscriber findOneByProductIdAndMsisdn(String id, String msisdn);

    List<Subscriber> findAllByProductIdAndStatusBetween(String id, int statusProcessed, int statusExpired);

    List<Subscriber> findAllByProductIdAndChargeNextTimeLessThanOrderByStatusDesc(String id, ZonedDateTime now);

    Page<Subscriber> findAllByProductIdAndChargeNextTimeLessThanOrderByStatusDesc(String id, ZonedDateTime now,
            Pageable page);

    Page<Subscriber> findAllByProductIdAndStatusLessThanAndChargeNextTimeLessThanOrderByStatusDesc(String id,
            int statusLeft, ZonedDateTime now, Pageable page);

    List<Subscriber> findAllByProductIdAndStatusLessThanAndChargeNextTimeLessThanOrderByStatusDesc(String id,
            int statusProcessed, ZonedDateTime now);

    List<Subscriber> findAllByProductIdAndStatusBetweenAndChargeNextTimeLessThanOrderByStatusDesc(String id,
            int statusExpired, int statusProcessed, ZonedDateTime now);

    Page<Subscriber> findAllByProductIdAndStatusBetweenAndChargeNextTimeLessThanOrderByStatusDesc(String id,
            int statusExpired, int statusProcessed, ZonedDateTime now, Pageable page);

    int deleteAllByMsisdn(String msisdn);

    Subscriber findOneByMsisdnAndStatus(String msisdn, int statusNeedConfirm);

    Page<Subscriber> findAllByStatusLessThanAndProductIdAndChargeNextTimeLessThan(int statusProcessed, String cid,
            ZonedDateTime now, Pageable pagable);

    List<Subscriber> findAllByNotifyAndChargeLastTimeLessThan(int notifyProcessed, ZonedDateTime now);

    List<Subscriber> findAllByProductIdAndNotifyBetweenAndStatusBetweenAndChargeNextTimeLessThanOrderByStatusDesc(
            String id, int notifyExpired, int notifyPending, int statusExpired, int statusPending,
            ZonedDateTime plusDays);

    List<Subscriber> findAllByProductIdAndStatusBetweenAndNotifyAndChargeNextTimeLessThanOrderByStatusDesc(String id,
            int statusExpired, int statusPending, int notifyDone, ZonedDateTime now);

    Page<Subscriber> findAllByNotifyBetweenAndStatusBetweenAndChargeNextTimeLessThanOrderByStatusDesc(int notifyExpired,
            int notifyProcessed, int statusExpired, int statusLeft, ZonedDateTime plusDays, Pageable pageable);

    List<Subscriber> findAllByProductIdAndStatus(String id, int statusChargeSuccess);

    List<Subscriber> findAllByProductIdAndStatusLessThanAndExpiryTimeGreaterThan(String id, int statusLeft,
            ZonedDateTime now);

    List<Subscriber> findAllByMsisdn(String msisdn);

    int deleteAllByChargeLastSuccessIsNullOrChargeLastSuccessLessThan(ZonedDateTime minusMonths);
}
