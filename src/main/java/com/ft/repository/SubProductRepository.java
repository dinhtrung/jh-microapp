package com.ft.repository;

import org.springframework.stereotype.Repository;

import com.ft.domain.Product;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Product entity.
 */
@Repository
public interface SubProductRepository extends MongoRepository<Product, String>, SubProductCustomRepository {

	Product findOneByIdAndCode(String serviceID, String productID);

	Product findOneByJoinPatternLikeAndJoinChannelLike(String productID, String dnis);

	Product findOneByLeftPatternLike(String productID);

	Product findOneByJoinPatternIgnoreCase(String shortCode, String productName);

	Product findOneByJoinPatternLike(String shortMsg);

	Product findOneByLeftPattern(String shortMsg);

	Product findOneByJoinPatternIgnoreCase(String shortMsg);

	Product findOneByLeftPatternIgnoreCase(String shortMsg);

}
