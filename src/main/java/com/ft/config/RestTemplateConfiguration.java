package com.ft.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.ft.service.SmppProcessService;
import com.ft.smpp.OutboundClient;
import com.ft.smpp.SmppClientMessageService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Configuration
public class RestTemplateConfiguration {

	private final Logger log = LoggerFactory.getLogger(RestTemplateConfiguration.class);

	@Autowired
	private ApplicationProperties props;

	/**
	 * Configuration for RestTemplate Charging Client
	 * @return
	 */
	@Bean
	public RestTemplate chargingRestTemplate() {
		RestTemplate  restTemplate = new RestTemplate();
		// IMPORTANT: This request factory get the response back
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
	    return restTemplate;
	}


	/**
     * Bucket4j RateLimitter
     */
//    @Bean
//    public Bucket chargingTpsBucket(){
//    	Bandwidth limit = Bandwidth.simple(props.getChargingClientTps(), Duration.ofSeconds(1));
//    	// construct the bucket
//    	Bucket bucket = Bucket4j.builder().addLimit(limit).build();
//    	log.info("=== CHARGING TPS: " + props.getChargingClientTps());
//    	return bucket;
//    }

}
