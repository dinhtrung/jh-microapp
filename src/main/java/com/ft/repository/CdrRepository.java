package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Cdr;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Cdr entity.
 */
@Repository
public interface CdrRepository extends MongoRepository<Cdr, String>, CdrCustomRepository {

}
