package com.ft.service.dto;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Sms.
 */
public class SmsDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Size(max = 40)
    private String source;

    @Size(max = 40)
    private String destination;

    private String text;

    private String messageId;

    private String productId;

    private ZonedDateTime submitAt;

    private ZonedDateTime deliveredAt;

    @Min(value = -9)
    @Max(value = 9)
    private Integer status;

    public static final int STATE_PENDING = 0;
    public static final int STATE_SUBMITTED = 1;
    public static final int STATE_DELIVERED = 2;
    public static final int STATE_FAILED = -9;
	public static final int STATE_PROCESSED = 3;

    private String tag;

    private String op;

	public SmsDTO(SmsDTO sms) {
		this.source = sms.source;
		this.destination = sms.destination;
		this.text = sms.text;
		this.status = 0;
	}

	public SmsDTO() {
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

    public SmsDTO source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public SmsDTO destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public SmsDTO text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageId() {
        return messageId;
    }

    public SmsDTO messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public SmsDTO submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public SmsDTO deliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        return this;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Integer getStatus() {
        return status;
    }

    public SmsDTO status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public SmsDTO tag(String tag) {
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
  	public SmsDTO productId(String productId) {
  		this.productId = productId;
          return this;
      }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmsDTO sms = (SmsDTO) o;
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
            ", op='" + getOp() + "'" +
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
