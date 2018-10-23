package com.ft.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to App.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
public class ApplicationProperties {
	
	private String serverUrl = "http://localhost/api/soap-service";

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	private String webroot = "/ws/*";

	public String getWebroot() {
		return webroot;
	}

	public void setWebroot(String webroot) {
		this.webroot = webroot;
	}
	

}
