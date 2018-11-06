package com.ft.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ft.domain.Product;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
@Repository
public interface SubProductCustomRepository {

	List<Product> stats();

	Criteria createCriteria(Product searchModel);

	Query createQuery(Product searchModel);

	Page<Product> findAll(Query query, Pageable pageable);

	Page<Product> findAll(String query, Pageable pageable);

	List<Product> stats(Product searchModel);

	List<Product> search(Product searchModel);
}
