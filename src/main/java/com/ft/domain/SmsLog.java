package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SmsLog.
 */
@Document(collection = "sms_log")
@CompoundIndexes({
    @CompoundIndex(name = "sms_msisdn_content",
                   unique = true,
                   def = "{'destination' : 1, 'content_id' : 1}")
})
public class SmsLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Size(max = 40)
    @Field("source")
    private String source;

    @Size(max = 40)
    @Field("destination")
    @Indexed
    private String destination;

    @Field("text")
    private String text;

    @Field("message_id")
    @Indexed
    private String messageId;

    @Field("product_id")
    @Indexed
    private String productId;

    @Field("content_id")
    private String contentId;

    @Field("submit_at")
    @Indexed
    private ZonedDateTime submitAt;

    @Field("delivered_at")
    private ZonedDateTime deliveredAt;

    @Min(value = -9)
    @Max(value = 9)
    @Field("status")
    @Indexed
    private Integer status;

    public static final int STATE_PENDING = 0;
    public static final int STATE_SUBMITTED = 1;
    public static final int STATE_DELIVERED = 2;
    public static final int STATE_FAILED = -9;
	public static final int STATE_PROCESSED = 3;

    @Field("tag")
    private String tag;

    public SmsLog(SmsLog sms) {
		this.source = sms.source;
		this.destination = sms.destination;
		this.text = sms.text;
		this.status = STATE_PENDING;
		this.submitAt = ZonedDateTime.now();
	}


	public SmsLog() {
		super();
		this.status = 0;
		this.submitAt = ZonedDateTime.now();
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

    public SmsLog source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public SmsLog destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public SmsLog text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageId() {
        return messageId;
    }

    public SmsLog messageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public SmsLog submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public SmsLog deliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
        return this;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Integer getStatus() {
        return status;
    }

    public SmsLog status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public SmsLog tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	public SmsLog productId(String productId) {
		this.productId = productId;
        return this;
    }

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public SmsLog contentId(String contentId) {
		this.contentId = contentId;
		return this;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SmsLog smsLog = (SmsLog) o;
        if (smsLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsLog{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", destination='" + getDestination() + "'" +
            ", text='" + getText() + "'" +
            ", messageId='" + getMessageId() + "'" +
            ", productId='" + getProductId() + "'" +
            ", contentId='" + getContentId() + "'" +
            ", messageId='" + getMessageId() + "'" +
            ", submitAt='" + getSubmitAt() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
