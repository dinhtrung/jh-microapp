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
	
	private String webroot = "/ws/*";

	public String getWebroot() {
		return webroot;
	}

	public void setWebroot(String webroot) {
		this.webroot = webroot;
	}
	
	private String clUrl;
	private String isdUrl;
	private String luUrl;

	public String getClUrl() {
		return clUrl;
	}

	public void setClUrl(String clUrl) {
		this.clUrl = clUrl;
	}

	public String getIsdUrl() {
		return isdUrl;
	}

	public void setIsdUrl(String isdUrl) {
		this.isdUrl = isdUrl;
	}

	public String getLuUrl() {
		return luUrl;
	}

	public void setLuUrl(String luUrl) {
		this.luUrl = luUrl;
	}

}
