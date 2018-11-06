package com.ft.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ft.domain.Provisioning;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Dec 1, 2017, 8:09:42 PM
 * @Quote To code is human, to debug is coffee
 *
 */
@Repository
public interface ProvisioningRepository extends MongoRepository<Provisioning, String> {

    Long countByStatusEqualsAndDateLoggedIs(String status, Date date);

    Long countByDateLoggedIs(Date date);

    Provisioning findOneByProductIdAndContentIdAndMsisdnAndDateLogged(String productId, String contentId,
            String msisdn, Date today);
    
    List<Provisioning> findAllByProductIdAndContentIdAndMsisdnAndDateLogged(String productId, String contentId,
            String msisdn, Date today);

    Long countByDateLoggedIsAndStatusContainingIgnoreCase(Date date, String status);

    Long countByProductIdAndContentIdAndDateLogged(String productId, String contentId, Date today);

    Page<Provisioning> findAllByStatusEqualsOrStatusEqualsAndDateLoggedEqualsAndNextRetrialBeforeOrderByTrialCount(String fresh, String failed,
            Date dateLogged, Date nextRetial, Pageable page);

    Page<Provisioning> findAllByStatusInAndDateLoggedEqualsAndNextRetrialBeforeOrderByTrialCount(Collection<String> statuses,
            Date dateLogged, Date nextRetial, Pageable page);

    Page<Provisioning> findAllByStatusInAndDateLoggedEqualsAndNextRetrialBeforeOrderBySuccessAverageDescTrialCountAsc(
            Collection<String> statuses, Date dateLogged, Date nextRetial, Pageable page);

    Long countByProductIdAndDateLogged(String productId, Date date);

    Long countByDateLoggedIsAndProductIdIsAndStatusContainingIgnoreCase(Date date, String productId,
            String status);

}
