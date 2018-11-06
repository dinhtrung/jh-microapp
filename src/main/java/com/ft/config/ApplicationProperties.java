package com.ft.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.ft.domain.Product;
import com.ft.domain.SmsLog;

/**
 * Properties specific to App.
 * <p>
 * Properties are configured in the application.yml file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    /**
     * Rate Limiter for SMSC
     */
    private SmsLog sms;

    private List<SmppSessionConfiguration> smppConfig = new ArrayList<SmppSessionConfiguration>();

    public List<SmppSessionConfiguration> getSmppConfig() {
        return smppConfig;
    }

    public void setSmppConfig(List<SmppSessionConfiguration> smppConfig) {
        this.smppConfig = smppConfig;
    }

    private List<SmppSessionConfiguration> chargingConfig = new ArrayList<SmppSessionConfiguration>();

    public List<SmppSessionConfiguration> getChargingConfig() {
        return chargingConfig;
    }

    public void setChargingConfig(List<SmppSessionConfiguration> chargingConfig) {
        this.chargingConfig = chargingConfig;
    }

    public SmsLog getSms() {
        return sms;
    }

    public void setSms(SmsLog sms) {
        this.sms = sms;
    }

    /**
     * Mobile Country Code
     */
    private String mcc = "234";

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    /**
     * Return the correct MSISDN format for the whole number
     *
     * @param msisdn
     * @return
     */
    public String msisdnFormat(String msisdn) {
        msisdn = "" + Long.parseLong(msisdn.trim());
        if (!msisdn.substring(0, getMcc().length()).equals(getMcc())) {
            msisdn = getMcc() + msisdn;
        }
        return String.valueOf(Long.parseLong(msisdn));
    }

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

    /**
     * SMPP Submit SM Parameters
     */
    private SubmitSm chargingSm;

    public SubmitSm getChargingSm() {
        return chargingSm;
    }

    public void setChargingSm(SubmitSm chargingSm) {
        this.chargingSm = chargingSm;
    }

    /**
     * Default Product Template
     */
    private Product productTpl;

    public Product getProductTpl() {
        return productTpl;
    }

    public void setProductTpl(Product productTpl) {
        this.productTpl = productTpl;
    }

    /**
     * Batch Processing Routine
     */
    private int batchSize = 2000;

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
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

    /**
     * Rate Limiter for Charging Client
     */
    private long chargingClientTps = 10;

    public long getChargingClientTps() {
        return chargingClientTps;
    }

    public void setChargingClientTps(long chargingClientTps) {
        this.chargingClientTps = chargingClientTps;
    }

    private boolean doubleOptIn = true;

    public boolean isDoubleOptIn() {
        return doubleOptIn;
    }

    public void setDoubleOptIn(boolean doubleOptIn) {
        this.doubleOptIn = doubleOptIn;
    }

    private boolean chargePerSms = true;

    public boolean isChargePerSms() {
        return chargePerSms;
    }

    public void setChargePerSms(boolean chargePerSms) {
        this.chargePerSms = chargePerSms;
    }

    private boolean preChargeNotify = false;

    public boolean isPreChargeNotify() {
        return preChargeNotify;
    }

    public void setPreChargeNotify(boolean preChargeNotify) {
        this.preChargeNotify = preChargeNotify;
    }

    private boolean chargeWhenSubscribe = true;

    public boolean isChargeWhenSubscribe() {
        return chargeWhenSubscribe;
    }

    public void setChargeWhenSubscribe(boolean chargeWhenSubscribe) {
        this.chargeWhenSubscribe = chargeWhenSubscribe;
    }

    private boolean notifyChargeFailed = false;

    public boolean isNotifyChargeFailed() {
        return notifyChargeFailed;
    }

    public void setNotifyChargeFailed(boolean notifyChargeFailed) {
        this.notifyChargeFailed = notifyChargeFailed;
    }

//	Etisalat new charging gateway
    private String chargingUrl;

    public String getChargingUrl() {
        return chargingUrl;
    }

    public void setChargingUrl(String chargingUrl) {
        this.chargingUrl = chargingUrl;
    }

    private Map<String, String> chargingHeaders;

    public Map<String, String> getChargingHeaders() {
        return chargingHeaders;
    }

    public void setChargingHeaders(Map<String, String> chargingHeaders) {
        this.chargingHeaders = chargingHeaders;
    }

}
