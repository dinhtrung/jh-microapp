package com.ft.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.domain.Cdr;
import com.ft.domain.CdrStat;
import com.ft.domain.Product;
import com.ft.domain.SmsLog;
import com.ft.repository.CdrRepository;
import com.ft.repository.CdrStatRepository;
import com.ft.repository.SmsLogRepository;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;
import com.ft.service.dto.DashboardDTO;


/**
 * Service Implementation for managing CdrStat.
 */
@Service
@EnableScheduling
public class CdrStatService {

    private final Logger log = LoggerFactory.getLogger(CdrStatService.class);
    private final CdrRepository cdrRepository;
    private final CdrStatRepository cdrStatRepository;
    private final SubscriberRepository subMsisdnRepository;
    private final SmsLogRepository smsLogRepository;
    private final SubProductRepository subProductRepository;

    public CdrStatService(CdrRepository cdrRepository, CdrStatRepository cdrStatRepository,
			SubscriberRepository subMsisdnRepository, SmsLogRepository smsLogRepository,
			SubProductRepository subProductRepository) {
		super();
		this.cdrRepository = cdrRepository;
		this.cdrStatRepository = cdrStatRepository;
		this.subMsisdnRepository = subMsisdnRepository;
		this.smsLogRepository = smsLogRepository;
		this.subProductRepository = subProductRepository;
	}

	/**
     * Save a cdrStat.
     *
     * @param cdrStat the entity to save
     * @return the persisted entity
     */
    public CdrStat save(CdrStat cdrStat) {
        log.debug("Request to save CdrStat : {}", cdrStat);
        return cdrStatRepository.save(cdrStat);
    }

    /**
     *  Get all the cdrStats.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<CdrStat> findAll(Pageable pageable) {
        log.debug("Request to get all CdrStats");
        return cdrStatRepository.findAll(pageable);
    }

    /**
     *  Get one cdrStat by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Optional<CdrStat> findOne(String id) {
        log.debug("Request to get CdrStat : {}", id);
        return cdrStatRepository.findById(id);
    }

    /**
     *  Delete the  cdrStat by id.
     *
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete CdrStat : {}", id);
        cdrStatRepository.deleteById(id);
    }

    @Scheduled(cron="0 0 * * * *")
    public List<CdrStat> caculatorStatAndBackupCdr() {
    	//remove false status
    	cdrRepository.removeFalseCdr();
    	Cdr searchModel = new Cdr()
				.requestAt(
						ZonedDateTime.now()
						.withDayOfMonth(-2)
						.withHour(0)
						.withMinute(0)
						.withSecond(0)
				).status(true);

    	List<CdrStat> cdrStats = cdrRepository.getStats(searchModel);
    	//save to cdrStats
    	cdrStatRepository.saveAll(cdrStats);

    	//TODO:create backup

    	//TODO: remove from crd
    	return cdrStats;
	}
    public List<CdrStat> getStatFromCdr() {
    	cdrRepository.removeFalseCdr();
    	cdrStatRepository.deleteAll();
    	Cdr searchModel = new Cdr().status(true);
    	List<CdrStat> cdrStats = cdrRepository.getStats(searchModel);
    	cdrStatRepository.saveAll(cdrStats);
    	return cdrStats;
	}

	private Map<String, Product> getMapSubProduct() {
		Map<String, Product> result = new HashMap<>();
		List<Product> listProduct = subProductRepository.findAll();
		for (Product subProduct : listProduct) {
			result.put(subProduct.getId(), subProduct);
		}
		return result;
	}

	public Map<String, Object> getDashboard(ZonedDateTime from, ZonedDateTime to) {
		log.debug("Gonna generate data for dashboard from " + from + " --> " + to);
		Map<String, Object> dashboard = new HashMap<>();
//		Map<String, Product> mapProduct = getMapSubProduct();
//		dashboard.put("mapProduct", mapProduct);

		List<CdrStat> cdrStats = getDashboardStat(from, to);
//		Map<String, List<DashboardDTO>> mapSysRevalue = getMapChartStat(mapProduct, cdrStats);
		Collection<CdrStat> sysRevalue = calculateSysRevenue (cdrStats);
		dashboard.put("sysRevalue", sysRevalue);

//		Collection<DashboardDTO> revenuePerDate = calculateRevenuePerDate (cdrStats);
//		dashboard.put("revenuePerDate", revenuePerDate);

		List<DashboardDTO> joinSub =  subMsisdnRepository.dashboardJoinSub(from, to);
//		Map<String, List<DashboardDTO>> mapJoinSub = getMapDashboardByDate(mapProduct, joinSub);
		dashboard.put("joinSub", joinSub);

		List<DashboardDTO> leftSub =  subMsisdnRepository.dashboardLeftSub(from, to);
//		Map<String, List<DashboardDTO>> mapLeftSub = getMapDashboardByDate(mapProduct, leftSub);
		dashboard.put("leftSub", leftSub);

		SmsLog searchModel = new SmsLog().deliveredAt(to).submitAt(from);
		List<DashboardDTO> smsPerDate = smsLogRepository.stats(searchModel);
//		Map<String, List<DashboardDTO>> mapSmsPerDate = getMapDashboardByCode(mapProduct, smsPerDate);
		dashboard.put("smsPerDate", smsPerDate);

		return dashboard;
	}

//	private Map<String, List<DashboardDTO>> getMapChartStat(Map<String, Product> mapProduct,
//			List<CdrStat> cdrStats){
//		Map<String, List<DashboardDTO>> result = new HashMap<>();
//		for (CdrStat stat : cdrStats) {
//			DashboardDTO DashboardDTO = new DashboardDTO()
//											.date(stat.getDate())
//											.productId(stat.getRefId())
//											.revenue(stat.getRevenue())
//											.cnt(stat.getCnt().intValue());
//			Product product = mapProduct.get(DashboardDTO.getProductId());
//			if (product != null) {
//				DashboardDTO.setProductCode(product.getCode());
//			}
//			String key = DashboardDTO.getProductCode() == null ? "" : DashboardDTO.getProductCode();
//			List<DashboardDTO> listOnDate =  result.get(key);
//			if(listOnDate==null) {
//				listOnDate = new ArrayList<>();
//				result.put(key, listOnDate);
//			}
//			listOnDate.add(DashboardDTO);
//		}
//		return result;
//
//	}

	private Map<String, List<DashboardDTO>> getMapDashboardByCode(Map<String, Product> mapProduct,
			List<DashboardDTO> listDas) {
		Map<String, List<DashboardDTO>> result = new HashMap<>();
		for (DashboardDTO DashboardDTO : listDas) {
			Product product = mapProduct.get(DashboardDTO.getProductId());
			if (product != null) {
				DashboardDTO.setProductCode(product.getCode());
			}
			String key = DashboardDTO.getProductCode() == null ? "" : DashboardDTO.getProductCode();
			List<DashboardDTO> listOnDate =  result.get(key);
			if(listOnDate==null) {
				listOnDate = new ArrayList<>();
				result.put(key, listOnDate);
			}
			listOnDate.add(DashboardDTO);
		}
		return result;
	}

	/**
	 * Return list of DashboardDTO object for summary per date
	 * @param mapProduct
	 * @param listDas
	 * @return
	 */
	private Map<String, List<DashboardDTO>> getMapDashboardByDate(Map<String, Product> mapProduct,
			List<DashboardDTO> listDas) {
		Map<String, List<DashboardDTO>> result = new HashMap<>();
		for (DashboardDTO DashboardDTO : listDas) {
			Product product = mapProduct.get(DashboardDTO.getProductId());
			if (product != null) {
				DashboardDTO.setProductCode(product.getCode());
			}
			String key = DashboardDTO.getDate() == null ? "" : DashboardDTO.getDate();
			List<DashboardDTO> listOnDate =  result.get(key);
			if(listOnDate==null) {
				listOnDate = new ArrayList<>();
				result.put(key, listOnDate);
			}
			listOnDate.add(DashboardDTO);
		}
		return result;
	}

	/**
	 * Return sum data by ref / date
	 * @param cdrStats
	 * @return
	 */
	private Collection<DashboardDTO> calculateRevenuePerDate (List<CdrStat> cdrStats){
		if(cdrStats== null) {
			return new ArrayList<>();
		}
		Map<String, DashboardDTO> mapSumRevenue = new HashMap<>();
		for (CdrStat stat : cdrStats) {
			String date = stat.getDateTime().substring(0, 10);//2017-10-10
			DashboardDTO sumRevenue = mapSumRevenue.get(date);
			if (sumRevenue == null) {
				sumRevenue = new DashboardDTO(date, 0, 0D);
				mapSumRevenue.put(date, sumRevenue);
			}

			sumRevenue.setCnt(sumRevenue.getCnt()+stat.getCnt().intValue());
			sumRevenue.setRevenue(sumRevenue.getRevenue()+stat.getRevenue());
		}

		List<DashboardDTO> result =new ArrayList<>(mapSumRevenue.values()) ;

		Collections.sort(result, new Comparator<DashboardDTO>() {
			@Override
			public int compare(DashboardDTO o1, DashboardDTO o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		return result;
	}

	private List<CdrStat> calculateSysRevenue (List<CdrStat> cdrStats){
		if(cdrStats== null) {
			return new ArrayList<>();
		}
		Map<String, CdrStat> mapSumRevenue = new HashMap<>();
		for (CdrStat stat : cdrStats) {
			String key = stat.getId().substring(0, stat.getId().length() - 3);//2017-10-10
			String date = stat.getDateTime().substring(0, 10);
			CdrStat sumRevenue = mapSumRevenue.get(key);
			if (sumRevenue == null) {
				sumRevenue = new CdrStat()
						.dateTime(date)
						.cnt(0L).revenue(0D)
						.refId(stat.getRefId())
						.refType(stat.getRefType());
				mapSumRevenue.put(key, sumRevenue);
			}
			sumRevenue.setCnt(sumRevenue.getCnt()+stat.getCnt().intValue());
			sumRevenue.setRevenue(sumRevenue.getRevenue()+stat.getRevenue());
		}

		List<CdrStat> result =new ArrayList<>(mapSumRevenue.values()) ;

		Collections.sort(result, new Comparator<CdrStat>() {
			@Override
			public int compare(CdrStat o1, CdrStat o2) {
				return o2.getDateTime().compareTo(o1.getDateTime());
			}
		});

		return result;
	}
	private List<CdrStat> getDashboardStat(ZonedDateTime from, ZonedDateTime to) {
		List<CdrStat> cdrStats = null;
		ZonedDateTime dateBeforNow3Day = ZonedDateTime.now().minusDays(3);

		Cdr searchModel = null;
		if(to == null || dateBeforNow3Day.isBefore(to)) {
			if(from!=null && dateBeforNow3Day.isBefore(from)) {
				searchModel = new Cdr()
						.requestAt(from)
						.responseAt(to)
						.status(true);
				cdrStats = cdrRepository.getStats(searchModel);

			} else {
				cdrStats = new ArrayList<>();
				searchModel = new Cdr()
						.requestAt(from)
						.responseAt(to);
				List<CdrStat> statAfter3Day = cdrStatRepository.dashboardStats(searchModel);
				cdrStats.addAll(statAfter3Day);

				searchModel = new Cdr()
						.requestAt(from)
						.responseAt(to)
						.status(true);
				List<CdrStat> statBefor3Day = cdrRepository.getStats(searchModel);
				cdrStats.addAll(statBefor3Day);

			}
		} else {
			searchModel = new Cdr()
					.requestAt(from)
					.responseAt(to);
			cdrStats = cdrStatRepository.dashboardStats(searchModel);

		}
		return cdrStats;
	}

	public Page<CdrStat> findAll(String query, Pageable pageable) {
		return cdrStatRepository.findAll(query, pageable);
	}
}
