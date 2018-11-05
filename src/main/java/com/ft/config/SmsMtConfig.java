package com.ft.config;

import java.time.Duration;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.commons.util.LoadBalancedList;
import com.cloudhopper.commons.util.LoadBalancedLists;
import com.cloudhopper.commons.util.RoundRobinLoadBalancedList;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.ft.service.SmppProcessService;
import com.ft.smpp.OutboundClient;
import com.ft.smpp.ReconnectionDaemon;
import com.ft.smpp.SmppClientMessageService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;

@Configuration
@AutoConfigureAfter({ WebConfigurer.class })
public class SmsMtConfig {

	private static final Logger log = LoggerFactory.getLogger(SmsMtConfig.class);

	@Autowired
	private ApplicationProperties props;

	@Autowired
	private SmppProcessService smppProcessor;

	public static LoadBalancedList<OutboundClient> balancedList = LoadBalancedLists.synchronizedList(new RoundRobinLoadBalancedList<OutboundClient>());


	@PostConstruct
	protected void initialize(){
		initializeSmppConn();
	}

	private void initializeSmppConn() {
		log.info("Trying to connect to our SMSC...");
		for (SmppSessionConfiguration cfg : props.getSmppConfig()){
			OutboundClient conn = new OutboundClient();
			conn.initialize(cfg, new SmppClientMessageService(smppProcessor), props);
			conn.scheduleReconnect();
			log.info("Add new session: " + conn.getSessionId() + " --- " + conn.getConfiguration().getHost() + " : " + conn.getConfiguration().getPort());
			balancedList.set(conn, 1);
		}
	}

	public SubmitSmResp sendMessage(String source, String text, String destination) throws Exception {
		SubmitSm request = copySubmitSm(props.getSubmitSm());
		request.getSourceAddress().setAddress(source);
		request.getDestAddress().setAddress(destination);
		request.setShortMessage(CharsetUtil.encode(text, CharsetUtil.CHARSET_GSM));
		try {
			return sendMessage(request);
	        } catch (Exception e){
			log.error("Cannot send SMS: " + source + " --> " + destination + ": " + text, e);
			throw e;
		}
	}

	public static SubmitSmResp sendMessage(SubmitSm request) throws Exception {
		while (true) {
			final OutboundClient next = balancedList.getNext();
			final SmppSession session = next.getSession();
			if (session != null && session.isBound()) {
				return session.submit(request, 10000);
			}
		}
	}

	public static SubmitSm copySubmitSm(SubmitSm src) {
		SubmitSm request = new SubmitSm();
		request.setDestAddress(src.getDestAddress());
		request.setSourceAddress(src.getSourceAddress());
		request.setPriority(src.getPriority());
		request.setProtocolId(src.getProtocolId());
		request.setEsmClass(src.getEsmClass());
		request.setDataCoding(src.getDataCoding());
		request.setRegisteredDelivery(src.getRegisteredDelivery());
		return request;
	}

	@PreDestroy
	protected void stopAll() throws Throwable{
		ReconnectionDaemon.getInstance().shutdown();
		for (LoadBalancedList.Node<OutboundClient> node : balancedList.getValues()) {
			node.getValue().shutdown();
		}
	}

	 /**
     * Bucket4j RateLimitter
     */
    @Bean
    public Bucket submitSmTpsBucket(){
    	Bandwidth limit = Bandwidth.simple(props.getSubmitSmTps(), Duration.ofSeconds(1));
    	// construct the bucket
    	Bucket bucket = Bucket4j.builder().addLimit(limit).build();
    	log.info("=== SMPP TPS: " + props.getSubmitSmTps());
    	return bucket;
    }

    /**
     * Executor for SMS
     * @return
     */
    @Bean(name = "smsSendingExecutor")
    public Executor smsSendingExecutor() {
        log.debug("Creating SMS Sending Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setDaemon(true);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("submitSm-Executor-");
        return (executor);
    }

}
