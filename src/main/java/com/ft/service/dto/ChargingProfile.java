package com.ft.service.dto;

public class ChargingProfile {

	/**
	 * How many days will we do the charge again
	 */
	private Integer chargePeriod;

	/**
	 * Unit price per charging
	 */
	private Integer chargePrice;

	/**
	 * How many days this user got free of charge
	 */
	private Integer chargeFree;

	/**
	 * How to connect to charger
	 */
	private String shortCode;

	/**
	 * Message to send to customer when charge
	 */
	private String message;

	/**
	 * Custom welcome message send to customer
	 */
	private String msgWelcome;


	public ChargingProfile() {
		super();
	}

	public ChargingProfile(Integer chargePeriod, Integer chargePrice, Integer chargeFree, String shortCode, String message) {
		super();
		this.chargePeriod = chargePeriod;
		this.chargePrice = chargePrice;
		this.chargeFree = chargeFree;
		this.shortCode = shortCode;
		this.message = message;
	}

	public Integer getChargePeriod() {
		return chargePeriod;
	}

	public void setChargePeriod(Integer chargePeriod) {
		this.chargePeriod = chargePeriod;
	}

	public Integer getChargePrice() {
		return chargePrice;
	}

	public void setChargePrice(Integer chargePrice) {
		this.chargePrice = chargePrice;
	}

	public Integer getChargeFree() {
		return chargeFree;
	}

	public void setChargeFree(Integer chargeFree) {
		this.chargeFree = chargeFree;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMsgWelcome() {
		return msgWelcome;
	}

	public void setMsgWelcome(String msgWelcome) {
		this.msgWelcome = msgWelcome;
	}


	@Override
	public String toString() {
		return "ChargingProfile [chargePeriod=" + chargePeriod + ", chargePrice=" + chargePrice + ", chargeFree="
				+ chargeFree + ", shortCode=" + shortCode + ", message=" + message + ", msgWelcome=" + msgWelcome
				+ ", requestTemplate=" + requestTemplate + "]";
	}


	private Object requestTemplate;

	public Object getRequestTemplate() {
		return requestTemplate;
	}

	public void setRequestTemplate(Object requestTemplate) {
		this.requestTemplate = requestTemplate;
	}

}
