package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.DataFile;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the DataFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFileRepository extends MongoRepository<DataFile, String> {

}
