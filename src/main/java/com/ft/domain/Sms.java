package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Sms.
 */
@Document(collection = "sms")
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Size(max = 40)
    @Field("source")
    private String source;

    @Size(max = 40)
    @Field("destination")
    private String destination;

    @Field("text")
    private String text;

    @Field("message_id")
    private String messageId;
    
    @Field("product_id")
    private String productId;

    @Field("submit_at")
    private ZonedDateTime submitAt;

    @Field("delivered_at")
    private ZonedDateTime deliveredAt;

    @Min(value = -9)
    @Max(value = 9)
    @Field("status")
    private Integer status;
    
    public static final int STATE_PENDING = 0;
    public static final int STATE_SUBMITTED = 1;
    public static final int STATE_DELIVERED = 2;
    public static final int STATE_FAILED = -9;
	public static final int STATE_PROCESSED = 3;

    @Field("tag")
    private String tag;

    public Sms(SmsLog sms) {
    	this.source = sms.getSource();
		this.destination = sms.getDestination();
		this.text = sms.getText();
		this.status = 0;
	}

	public Sms(Sms sms) {
		this.source = sms.source;
		this.destination = sms.destination;
		this.text = sms.text;
		this.status = 0;
	}

	public Sms() {
		super();
		this.status = 0;
		this.submitAt = ZonedDateTime.now();
		// TODO Auto-generated constructor stub
	}

	// jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public Sms source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public Sms destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public Sms text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageId() {
        return messageId;
    }

    public Sms messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public Sms submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public Sms deliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        return this;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Integer getStatus() {
        return status;
    }

    public Sms status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public Sms tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getProductId() {
  		return productId;
  	}

  	public void setProductId(String productId) {
  		this.productId = productId;
  	}
  	public Sms productId(String productId) {
  		this.productId = productId;
          return this;
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
        Sms sms = (Sms) o;
        if (sms.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sms.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sms{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", text='" + getText() + "'" +
            ", messageId='" + getMessageId() + "'" +
            ", productId='" + getProductId() + "'" +
            ", submitAt='" + getSubmitAt() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
