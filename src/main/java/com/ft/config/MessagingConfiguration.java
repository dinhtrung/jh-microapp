package com.ft.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ft.service.dto.SmsDTO;

/**
 * Configures Spring Cloud Stream support.
 *
 * This works out-of-the-box if you use the Docker Compose configuration at "src/main/docker/kafka.yml".
 *
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for the official Spring Cloud Stream documentation.
 */
@EnableBinding(value = { Processor.class })
public class MessagingConfiguration {

    @Value("${spring.application.name:JhipsterService}")
    private String applicationName;

    @Autowired
    RestTemplate restTemplate;

    private final Logger log = LoggerFactory.getLogger(MessagingConfiguration.class);

    /**
     * This sends a test message at regular intervals set as fixedRate (in ms)
     *
     * In order to see the test messages, you can use the Kafka command-line client:
     * "./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-jhipster --from-beginning".
     */
    @Bean
    @InboundChannelAdapter(value = Processor.OUTPUT, poller = @Poller(fixedRate = "60000"))
    public MessageSource<String> timerMessageSource() {
        return () -> new GenericMessage<>("Test message from " + applicationName
            + " sent at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @StreamListener(Processor.INPUT)
    public void smppAdapter(SmsDTO pdu) {
    	log.debug("Got message from broker: {}", pdu);
    	try {
    		ResponseEntity<Object> res = restTemplate.postForEntity("http://httpbin.org/post", pdu, Object.class);
    		log.info("RES: " + res.getBody());
    	} catch (HttpStatusCodeException e) {
    		log.error("ERR: {} -- {} | {}", e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString());
    	} catch (Exception f) {
    		log.error("Cannot send HTTP request with payload {} : {}", pdu, f);
    	}
    }
}
