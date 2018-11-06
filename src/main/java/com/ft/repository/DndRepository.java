package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Dnd;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Dnd entity.
 */
@Repository
public interface DndRepository extends MongoRepository<Dnd, String> {

	Page<Dnd> findAllByIdLike(String query, Pageable pageable);

}
