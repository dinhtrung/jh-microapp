package com.ft.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.ApplicationProperties;

@EnableScheduling
@Service
public class SoapServerService {

    private final Logger log = LoggerFactory.getLogger(SoapServerService.class);

    @Autowired
    private Executor scheduledTaskExecutor;

    @Autowired
    private ApplicationProperties props;


    /**
     * Execute every 15 minutes, from 08AM - 21PM everyday
     */
    @Scheduled(fixedDelay = 10000)
    public int submit() throws Exception {
    	return 0;
    }

    /**
     * Deliver SMS Response to customer every 10 seconds
     */
    @Scheduled(fixedDelay = 10000)
    public void sendSmsResponse() {
    	
    }
}
