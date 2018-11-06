package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.CdrStat;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the CdrStat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CdrStatRepository extends MongoRepository<CdrStat, String>, CdrStatCustomRepository {

}
