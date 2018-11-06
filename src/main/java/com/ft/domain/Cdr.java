package com.ft.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Cdr.
 */
@Document(collection = "cdr")
public class Cdr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("status")
    @Indexed
    private Boolean status;

    @Field("amount")
    private Double amount;

    @Field("partner_code")
    @Indexed
    private String partnerCode;

    @Field("product_id")
    @Indexed
    private String productId;

    @Field("content_id")
    @Indexed
    private String contentId;

    @Field("request_payload")
    private Object requestPayload;

    @Field("request_at")
    @Indexed
    private ZonedDateTime requestAt;

    @Field("response_payload")
    private Object responsePayload;

    @Field("response_at")
    private ZonedDateTime responseAt;

    @Field("note")
    private String note;

    @Field("msisdn")
    @Indexed
    private String msisdn;

    /**
     * Statistic Information
     */
    private Integer cnt;

    private Double revenue;

    private Object date;

    private Object ref;

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Object getDate() {
		return date;
	}

	public void setDate(Object date) {
		this.date = date;
	}

	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}

	// jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isStatus() {
        return status;
    }

    public Cdr status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public Cdr amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public Cdr partnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
        return this;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getProductId() {
        return productId;
    }

    public Cdr productId(String productId) {
        this.productId = productId;
        return this;
    }

	public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContentId() {
        return contentId;
    }

    public Cdr contentId(String contentId) {
        this.contentId = contentId;
        return this;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Object getRequestPayload() {
        return requestPayload;
    }

    public Cdr requestPayload(Object extraData) {
        this.requestPayload = extraData;
        return this;
    }

    public void setRequestPayload(Object requestPayload) {
        this.requestPayload = requestPayload;
    }

    public ZonedDateTime getRequestAt() {
        return requestAt;
    }

    public Cdr requestAt(ZonedDateTime requestAt) {
        this.requestAt = requestAt;
        return this;
    }

    public void setRequestAt(ZonedDateTime requestAt) {
        this.requestAt = requestAt;
    }

    public Object getResponsePayload() {
        return responsePayload;
    }

    public Cdr responsePayload(Object responsePayload) {
        this.responsePayload = responsePayload;
        return this;
    }

    public void setResponsePayload(Object responsePayload) {
        this.responsePayload = responsePayload;
    }

    public ZonedDateTime getResponseAt() {
        return responseAt;
    }

    public Cdr responseAt(ZonedDateTime responseAt) {
        this.responseAt = responseAt;
        return this;
    }

    public void setResponseAt(ZonedDateTime responseAt) {
        this.responseAt = responseAt;
    }

    public String getNote() {
        return note;
    }

    public Cdr note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Cdr msisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cdr cdr = (Cdr) o;
        if (cdr.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cdr.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cdr{" +
            "id=" + getId() +
            ", status='" + isStatus() + "'" +
            ", amount='" + getAmount() + "'" +
            ", partnerCode='" + getPartnerCode() + "'" +
            ", productId='" + getProductId() + "'" +
            ", contentId='" + getContentId() + "'" +
            ", requestPayload='" + getRequestPayload() + "'" +
            ", requestAt='" + getRequestAt() + "'" +
            ", responsePayload='" + getResponsePayload() + "'" +
            ", responseAt='" + getResponseAt() + "'" +
            ", note='" + getNote() + "'" +
            ", msisdn='" + getMsisdn() + "'" +
            "}";
    }
}
