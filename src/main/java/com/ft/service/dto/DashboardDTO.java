/**
 *
 */
package com.ft.service.dto;

/**
 * @author ducgiang8888
 *
 */
public class DashboardDTO {

	private String productId;
	private String productCode;
	private Integer cnt;
	private String date;
	private Double revenue;

	public DashboardDTO() {
	}

	public DashboardDTO(Integer cnt, String productId, String date) {
		super();
		this.cnt = cnt;
		this.productId = productId;
		this.date = date;
	}

	public DashboardDTO(String date, Integer cnt, Double revenue) {
		super();
		this.cnt = cnt;
		this.date = date;
		this.revenue = revenue;
	}

	public DashboardDTO(Integer cnt, String productId, String date, Double revenue) {
		super();
		this.cnt = cnt;
		this.productId = productId;
		this.date = date;
		this.revenue = revenue;
	}

	public DashboardDTO cnt(Integer cnt) {
		this.cnt = cnt;
		return this;
	}

	public DashboardDTO date(String date) {
		this.date = date;
		return this;
	}

	public DashboardDTO revenue(Double revenue) {
		this.revenue = revenue;
		return this;
	}

	public DashboardDTO productId(String productId) {
		this.productId = productId;
		return this;
	}

	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Override
	public String toString() {
		return "DashboardDTO [productId=" + productId + ", productCode=" + productCode + ", cnt=" + cnt + ", date="
				+ date + ", revenue=" + revenue + "]";
	}
}
