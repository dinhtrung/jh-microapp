package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Stat;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Stat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatRepository extends MongoRepository<Stat, String> {

}
