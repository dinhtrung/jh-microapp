package com.ft.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ft.component.BypassSSLSocketFactory;

import telsoft.app.object.SOAPRequest;
import telsoft.app.object.SOAPRequestService;

@Configuration
public class CxfConfiguration {

	private final Logger log = LoggerFactory.getLogger(CxfConfiguration.class);

	@Autowired
	ApplicationProperties props;

	@PostConstruct
	private void load() {
		log.info("================= CXF WEB SERVICE ENDPOINTS ======================");
		log.info("========== WEBROOT: " + props.getWebroot() + "==============");
		log.info("========== VASGATE URL: " + props.getVasgateEndpoint() + "==============");
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
	 * ================ Each endpoint and client are described here
	 * =================
	 */
	@Autowired
	private SOAPRequest vasgateRequest;

	@Bean
	public SOAPRequestService vasgateService() {
		SOAPRequestService svc = new SOAPRequestService();
		return svc;
	}

	@Bean
	public Endpoint endpointVasGateListener() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), vasgateRequest);
		endpoint.setServiceName(vasgateService().getServiceName());
		endpoint.setWsdlLocation(vasgateService().getWSDLDocumentLocation().toString());
		endpoint.publish("/vasgate");
		return endpoint;
	}

	@Bean
	public SOAPRequest vasgateClient() throws Exception {
		ClientProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(SOAPRequest.class);
		factory.setAddress(props.getVasgateEndpoint());
		SOAPRequest vasgateClient = (SOAPRequest) factory.create();
		Client client = ClientProxy.getClient(vasgateClient);
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("X-IBM-Client-Id", Arrays.asList("412c4625-5aed-459b-bf58-a10494129fc2"));
		client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers);
		HTTPConduit http = (HTTPConduit) client.getConduit();
		client.getInInterceptors().add(new LoggingInInterceptor());
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		http.setTlsClientParameters( new TLSClientParameters());
		http.getTlsClientParameters().setSSLSocketFactory(new BypassSSLSocketFactory());

		return vasgateClient;
	}

}
