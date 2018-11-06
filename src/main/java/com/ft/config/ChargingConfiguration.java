package com.ft.config;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.ft.component.HeaderRequestInterceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Configuration
public class ChargingConfiguration {

    private final Logger log = LoggerFactory.getLogger(ChargingConfiguration.class);

    @Autowired
    private ApplicationProperties props;

    /**
     * Bucket4j RateLimitter
     */
    @Bean
    public Bucket chargingTpsBucket() {
        Bandwidth limit = Bandwidth.simple(props.getChargingClientTps(), Duration.ofSeconds(1));
        // construct the bucket
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();
        //log.info("=== CHARGING TPS: " + props.getChargingClientTps());
        return bucket;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate result = new RestTemplate();
        result.getInterceptors().add(new HeaderRequestInterceptor(props.getChargingHeaders()));
        return result;
    }

}
