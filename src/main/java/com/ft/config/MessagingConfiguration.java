package com.ft.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;

import com.ft.domain.Sms;
import com.ft.repository.SmsRepository;

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
	
	private final Logger log = LoggerFactory.getLogger(MessagingConfiguration.class);

    @Value("${spring.application.name:sms}")
    private String applicationName;
    
    @Autowired
    private SmsRepository smsRepo;

    /**
     * Other SMS sender should return this message as SMS
     * @param sms
     */
    @StreamListener(target = Processor.INPUT, condition = "headers['" + MessageHeaders.CONTENT_TYPE + "']=='SMS'")
    public void handleSumissionResult(Sms sms) {
    	log.debug("Receive one SMS to save {}", sms);
    	smsRepo.save(sms);
    }
}
