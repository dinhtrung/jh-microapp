package com.ft.service.util;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.ft.domain.Product;
import com.ft.domain.Subscriber;
import com.ft.repository.SubProductRepository;
import com.ft.repository.SubscriberRepository;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Jul 27, 2018, 4:01:04 AM
 * @Quote To code is human, to debug is coffee
 */
@Service
public class CsvUtil {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SubProductRepository productRepo;
    @Autowired
    SubscriberRepository subscriberRepo;

    public void createNccCsv(int startIndex, int pageSize) throws Exception {
        ICsvMapWriter mapWriter = null;
        try {
            final String[] headers = new String[]{"SN", "DATE/TIME SUBSCRIBED", "DATE/TIME UNSUBSCRIBED",
                "MSISDN", "NAME OF VALUE ADDED SERVICE", "MOBILE NETWORK", "SHORT CODE",
                "SUBSCRIPTION MODE", "STATUS", "BILLING CYCLE"};
            mapWriter = new CsvMapWriter(
                    new FileWriter("/srv/etisalat/ncc_requested_data_" + startIndex + "_" + (startIndex + pageSize) + ".csv"),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
            final CellProcessor[] processors = getNccProcessors();
            // write the header
            mapWriter.writeHeader(headers);
            List<Product> products = productRepo.findAll();
            Map<String, Object> productCodes = new HashMap<>();
            products.stream().forEach((p) -> {
                productCodes.put(p.getId(), p);
            });
            Map<String, Object> map;
            List<Subscriber> subs;
            String msisdn;
            Product prod;
            Integer counter = 1;
            Page<Subscriber> page = subscriberRepo.findAll(new PageRequest(startIndex, pageSize));
            if (page != null) {
                subs = page.getContent();
                LOG.info("CsvUtil -->  Total subscribers:   " + subs.size());
                for (Subscriber sub : subs) {
                    if (sub == null || sub.getMsisdn() == null) {
                        System.out.println(counter + ". null subscriber");
                        continue;
                    }
                    msisdn = sub.getMsisdn().replaceAll("[^0-9]", "");
                    System.out.println(counter + ". " + msisdn);
                    map = new HashMap<>();
                    map.put(headers[0], (startIndex + counter));
                    map.put(headers[1], sub.getJoinAt());
                    map.put(headers[2], sub.getLeftAt());
                    map.put(headers[3], msisdn);
                    if (productCodes.get(sub.getProductId()) != null) {
                        prod = (Product) productCodes.get(sub.getProductId());
                        map.put(headers[4], prod.getCode());
                    } else {
                        map.put(headers[4], "Public Speaking");
                    }
                    map.put(headers[5], "Etisalat");
                    if (productCodes.get(sub.getProductId()) != null) {
                        prod = (Product) productCodes.get(sub.getProductId());
                        map.put(headers[6], prod.getBroadcastShortcode());
                    } else {
                        map.put(headers[6], "33085");
                    }
                    map.put(headers[7], sub.getJoinChannel() == null
                            || sub.getJoinChannel().equalsIgnoreCase("MAMO")
                            || sub.getJoinChannel().equalsIgnoreCase("DATA-FILE") ? "OBD" : sub.getJoinChannel());
                    map.put(headers[8], sub.getStatus() == 0 ? "ACTIVE" : "INACTIVE");
                    map.put(headers[9], "Daily");

                    mapWriter.write(map, headers, processors);
                    counter = counter + 1;
                }
            } else {
                LOG.info("CsvUtil -->  Paginated request returned NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.debug("CsvUtil Exception  -->", e);
        } finally {
            if (mapWriter != null) {
                mapWriter.close();
            }
        }
    }

    private CellProcessor[] getNccProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
            new Optional(new ParseInt()),
            new Optional(new FmtDate("yyyy/MM/dd:HH:mm:ss")),
            new Optional(new FmtDate("yyyy/MM/dd:HH:mm:ss")),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional(),
            new Optional()
        };
        return processors;
    }

}
