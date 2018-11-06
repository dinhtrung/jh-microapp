package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SmsContent.
 */
@Document(collection = "sms_content")
public class SmsContent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("start_at")
    @Indexed
    private ZonedDateTime startAt;

    @Field("expired_at")
    private ZonedDateTime expiredAt;

    @Size(max = 40)
    @Field("sender_address")
    @Indexed
    private String senderAddress;

    @Field("message")
    @Indexed
    private String message;

    @Min(value = -9)
    @Max(value = 9)
    @Field("status")
    @Indexed
    private Integer status;

    public static final int STATE_DISABLE = 0;
    public static final int STATE_EXPIRED = -9;
    public static final int STATE_ENABLE = 1;
    public static final int STATE_DONE = 9;

    @Size(max = 40)
    @Field("package_code")
    @Indexed
    private String packageCode;

    @Size(max = 40)
    @Field("product_id")
    private String productId;

    @Field("tag")
    private String tag;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getStartAt() {
        return startAt;
    }

    public SmsContent startAt(ZonedDateTime startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(ZonedDateTime startAt) {
        this.startAt = startAt;
    }

    public ZonedDateTime getExpiredAt() {
        return expiredAt;
    }

    public SmsContent expiredAt(ZonedDateTime expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public void setExpiredAt(ZonedDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public SmsContent senderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
        return this;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getMessage() {
        return message;
    }

    public SmsContent message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public SmsContent status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public SmsContent packageCode(String packageCode) {
        this.packageCode = packageCode;
        return this;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getProductId() {
        return productId;
    }

    public SmsContent productId(String productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTag() {
        return tag;
    }

    public SmsContent tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        SmsContent smsContent = (SmsContent) o;
        if (smsContent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsContent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsContent{" +
            "id=" + getId() +
            ", startAt='" + getStartAt() + "'" +
            ", expiredAt='" + getExpiredAt() + "'" +
            ", senderAddress='" + getSenderAddress() + "'" +
            ", message='" + getMessage() + "'" +
            ", status='" + getStatus() + "'" +
            ", packageCode='" + getPackageCode() + "'" +
            ", productId='" + getProductId() + "'" +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
