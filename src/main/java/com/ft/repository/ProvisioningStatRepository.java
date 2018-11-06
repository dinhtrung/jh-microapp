package com.ft.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ft.domain.ProvisioningStat;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date May 31, 2018, 6:28:00 AM
 * @Quote To code is human, to debug is coffee
 *
 */
@Repository
public interface ProvisioningStatRepository extends MongoRepository<ProvisioningStat, String> {

    List<ProvisioningStat> findAllByProvDate(Date date);

}
