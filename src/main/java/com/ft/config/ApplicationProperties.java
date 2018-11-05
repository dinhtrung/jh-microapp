package com.ft.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.SubmitSm;

/**
 * Properties specific to App.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	/**
	 * SMPP Submit SM Parameters
	 */
	private SubmitSm submitSm;

	public SubmitSm getSubmitSm() {
		return submitSm;
	}

	public void setSubmitSm(SubmitSm submitSm) {
		this.submitSm = submitSm;
	}

	private List<SmppSessionConfiguration> smppConfig = new ArrayList<SmppSessionConfiguration>();

	public List<SmppSessionConfiguration> getSmppConfig() {
		return smppConfig;
	}

	public void setSmppConfig(List<SmppSessionConfiguration> smppConfig) {
		this.smppConfig = smppConfig;
	}

	/**
	 * Rate Limiter for SMSC
	 */
	private long submitSmTps = 10;

	public long getSubmitSmTps() {
		return submitSmTps;
	}

	public void setSubmitSmTps(long submitSmTps) {
		this.submitSmTps = submitSmTps;
	}
}
