package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Partner;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Partner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerRepository extends MongoRepository<Partner, String> {

}
