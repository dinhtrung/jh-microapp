package com.ft.components;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.domain.Campaign;
import com.ft.domain.Sms;
import com.ft.repository.CampaignRepository;
import com.ft.repository.SmsRepository;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Component
@Scope("prototype")
public class SmsSubmitCallable implements Callable<Long> {

    private final Logger log = LoggerFactory.getLogger(SmsSubmitCallable.class);

    private List<Sms> smsList;

	private Campaign campaign;
	
	private Bucket bucket;

	@Autowired
    SmsRepository smsRepo;

    @Autowired
    CampaignRepository cpRepo;

    @Autowired
	RestTemplate restTemplate;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    MessageChannel output;

    @Override
    public Long call() throws Exception {
        this.bucket = createBucket(campaign.getRateLimit());
        for (Sms sms : smsList) {
        	if (Thread.currentThread().isInterrupted()) {
                // Cannot use InterruptedException since it's checked
                throw new RuntimeException();
            }
        	log.debug("Gotta submit SMS: {}", sms);
    	    try {
    	    	if (bucket != null) bucket.asScheduler().consume(1);
    	    	boolean submitResult = output.send(
    	    			MessageBuilder
    	    			.withPayload(sms)
    	    			.setHeader(MessageHeaders.CONTENT_TYPE, campaign.getChannel()).build()
    	    	);
    	    	log.debug("Submit result: {} sms: {}", submitResult, sms);
    	    	// Mark the SMS as Submission Pending / failed
    	    	sms
    	    	.state(submitResult ? 1 : -9)
    	    	.submitAt(ZonedDateTime.now()); 
    	    } catch (HttpStatusCodeException s){
    	    	log.error("Cannot send message: {} -- {} | {}", s.getRawStatusCode(), s.getStatusText(), s.getResponseBodyAsString());
    	    } catch (ResourceAccessException e) {
    			log.error("FAILED TO CONNECT TO ENDPOINT: {}", e.getMessage());
    	    } catch (RuntimeException | InterruptedException s) {
    	    	log.debug("Processing Interrupted. Exiting");
    	    	throw s;
            } catch (Exception e) {
                log.error("Exception: {}", e);
            }
        }
        long failedCnt = smsRepo.countByCampaignIdAndState(campaign.getId(), -9);
        long successCnt = smsRepo.countByCampaignIdAndState(campaign.getId(), 9);
    		campaign.getStats().put("failedCnt", failedCnt);
    		campaign.getStats().put("successCnt", successCnt);
    		campaign.getStats().put("submitCnt", successCnt + failedCnt);
		campaign = cpRepo.save(campaign);
        messagingTemplate.convertAndSend("/topic/campaign/" + campaign.getId(), campaign.getStats());
        messagingTemplate.convertAndSend("/topic/campaign", campaign);
        return successCnt + failedCnt;
    }
    
    public Bucket createBucket(Long tps) {
    	if (tps == null) return null;
        Bandwidth limit = Bandwidth.simple(tps, Duration.ofSeconds(1));
        // construct the bucket
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();
        return bucket;
    }


    public List<Sms> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<Sms> smsList) {
		this.smsList = smsList;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
}
