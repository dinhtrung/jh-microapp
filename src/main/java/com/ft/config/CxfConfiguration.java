package com.ft.config;

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tns.ns.SOAPMCA;
import tns.ns.SOAPMCA_Service;

@Configuration
public class CxfConfiguration {

	private final Logger log = LoggerFactory.getLogger(CxfConfiguration.class);

	@Autowired
	ApplicationProperties props;
	
	@PostConstruct
	private void load() {
		log.info("================= CXF WEB SERVICE ENDPOINTS ======================");
		log.info("========== WEBROOT: " + props.getWebroot() + "==============");
		log.info("========== SEND ISD: " + props.getIsdUrl() + "==============");
		log.info("========== SEND CL: " + props.getClUrl() + "==============");
		log.info("========== SEND LU: " + props.getLuUrl() + "==============");
	}

	@Bean
	public ServletRegistrationBean<CXFServlet> CxfDispatcherServlet() {
		return new ServletRegistrationBean<CXFServlet>(new CXFServlet(), props.getWebroot());
	}

	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		SpringBus springBus = new SpringBus();
        LoggingFeature logFeature = new LoggingFeature();
        logFeature.setPrettyLogging(true);
        logFeature.initialize(springBus);
        springBus.getFeatures().add(logFeature);
		return springBus;
	}
	
	/**
	 * ================   Each endpoint and client are described here =================
	 */
	 @Autowired
     private SOAPMCA soapMCA;


	@Bean
    public SOAPMCA_Service mcaService() {
         return new SOAPMCA_Service();
    }
    
    @Bean
    public Endpoint endpointMoListener() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), soapMCA);        
        endpoint.setServiceName(mcaService().getServiceName());
        endpoint.setWsdlLocation(mcaService().getWSDLDocumentLocation().toString());
        endpoint.publish("/mca");
//        LoggingFeature logFeature = new LoggingFeature();
//        logFeature.setPrettyLogging(true);
//        endpoint.getFeatures().add(logFeature);
        return endpoint;
    }
}
