package com.ft.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * A Subscriber.
 */
@Document(collection = "sub_msisdn")
@CompoundIndexes({
    @CompoundIndex(name = "msisdn_product",
            unique = true,
            def = "{'msisdn' : 1, 'product_id' : 1}")
})
public class Subscriber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    /**
     * The MSISDN number, with E164 formatted
     */
    @NotNull
    @Size(max = 40)
    @ApiModelProperty(value = "The MSISDN number, with E164 formatted", required = true)
    @Field("msisdn")
    @Indexed
    private String msisdn;

    @Field("trial_count")
    private Integer trialCount;

    @Field("success_count")
    private Integer successCount;

    /**
     * The subscriber status. 0: Pending for further process. -1 to -9: Failed
     * to process for -1 -9 times 1: Processed 2: Already Left
     */
    @Min(value = -9)
    @Max(value = 9)
    @ApiModelProperty(value = "The subscriber status. 0: Pending for further process. -1 to -9: Failed to process for -1 -9 times 1: Processed  2: Already Left")
    @Field("status")
    @NotNull
    @Indexed
    private Integer status;

    public static final int STATUS_EXPIRED = -9;
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_DONE_NOTIFY = 1;
    public static final int STATUS_PROCESSED = 2;
    public static final int STATUS_NEED_CONFIRM = 8;
    public static final int STATUS_LEFT = 9;

    /**
     * The subscriber status. 0: Pending for further process. -1 to -9: Failed
     * to process for -1 -9 times 1: Processed 2: Already Left
     */
    @Min(value = -9)
    @Max(value = 9)
    @ApiModelProperty(value = "The subscriber status. 0: Pending for further process. -1 to -9: Failed to process for -1 -9 times 1: Processed  9: Already Left")
    @Field("notify")
    @Indexed
    private Integer notify;
    public static final int NOTIFY_EXPIRED = -9;
    public static final int NOTIFY_PENDING = 0;
    public static final int NOTIFY_DONE = 2;
    public static final int NOTIFY_PROCESSED = 1;

    /**
     * WAP passwords
     */
    @ApiModelProperty(value = "WAP passwords")
    @Field("wap_password")
    private String wapPassword;

    /**
     * Product Associated
     */
    @NotNull
    @Size(max = 40)
    @ApiModelProperty(value = "Product Associated")
    @Field("product_id")
    @Indexed
    private String productId;

    /**
     * SubCP Associated
     */
    @Size(max = 40)
    @ApiModelProperty(value = "SubCP Associated")
    @Field("partner_code")
    private String partnerCode;
    @Field("reg_date")
    private Date regDate;
    @Size(max = 40)
    @Field("reg_channel")
    private String regChannel;

    /**
     * Joined information
     */
    @ApiModelProperty(value = "Joined information")
    @Field("join_at")
    private ZonedDateTime joinAt;

    @Size(max = 40)
    @Field("join_channel")
    private String joinChannel;

    /**
     * Leaving information
     */
    @ApiModelProperty(value = "Leaving information")
    @Field("left_at")
    private ZonedDateTime leftAt;

    @Size(max = 40)
    @Field("left_channel")
    private String leftChannel;

    /**
     * Charging related time
     */
    @ApiModelProperty(value = "Charging related time")
    @Field("charge_last_time")
    private ZonedDateTime chargeLastTime;

    @Field("charge_last_success")
    private ZonedDateTime chargeLastSuccess;

    @NotNull
    @Field("charge_next_time")
    @Indexed
    private ZonedDateTime chargeNextTime;

    /**
     * Charging notification related time
     */
    @ApiModelProperty(value = "Pre-charge last notification time")
    @Field("notify_last_time")
    private ZonedDateTime notifyLastTime;

    @Field("expiry_time")
    private ZonedDateTime expiryTime;

    /**
     * Additional Information
     */
    @ApiModelProperty(value = "Additional Information")
    @Field("tag")
    private String tag;

    /**
     * Additional Optional Info, JSON encoded ones
     */
    @ApiModelProperty(value = "Additional Optional Info, JSON encoded ones")
    @Field("extra_data")
    private String extraData;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Subscriber msisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getStatus() {
        return status;
    }

    public Subscriber status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWapPassword() {
        return wapPassword;
    }

    public Subscriber wapPassword(String wapPassword) {
        this.wapPassword = wapPassword;
        return this;
    }

    public void setWapPassword(String wapPassword) {
        this.wapPassword = wapPassword;
    }

    public String getProductId() {
        return productId;
    }

    public Subscriber productId(String productId) {
        this.productId = productId;
        return this;
    }

    public void setproductId(String productId) {
        this.productId = productId;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public Subscriber partnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
        return this;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public ZonedDateTime getJoinAt() {
        return joinAt;
    }

    public Subscriber joinAt(ZonedDateTime joinAt) {
        this.joinAt = joinAt;
        return this;
    }

    public void setJoinAt(ZonedDateTime joinAt) {
        this.joinAt = joinAt;
    }

    public String getJoinChannel() {
        return joinChannel;
    }

    public Subscriber joinChannel(String joinChannel) {
        this.joinChannel = joinChannel;
        return this;
    }

    public void setJoinChannel(String joinChannel) {
        this.joinChannel = joinChannel;
    }

    public ZonedDateTime getLeftAt() {
        return leftAt;
    }

    public Subscriber leftAt(ZonedDateTime leftAt) {
        this.leftAt = leftAt;
        return this;
    }

    public void setLeftAt(ZonedDateTime leftAt) {
        this.leftAt = leftAt;
    }

    public String getLeftChannel() {
        return leftChannel;
    }

    public Subscriber leftChannel(String leftChannel) {
        this.leftChannel = leftChannel;
        return this;
    }

    public void setLeftChannel(String leftChannel) {
        this.leftChannel = leftChannel;
    }

    public ZonedDateTime getChargeLastTime() {
        return chargeLastTime;
    }

    public Subscriber chargeLastTime(ZonedDateTime chargeLastTime) {
        this.chargeLastTime = chargeLastTime;
        return this;
    }

    public void setChargeLastTime(ZonedDateTime chargeLastTime) {
        this.chargeLastTime = chargeLastTime;
    }

    public ZonedDateTime getChargeLastSuccess() {
        return chargeLastSuccess;
    }

    public Subscriber chargeLastSuccess(ZonedDateTime chargeLastSuccess) {
        this.chargeLastSuccess = chargeLastSuccess;
        return this;
    }

    public void setChargeLastSuccess(ZonedDateTime chargeLastSuccess) {
        this.chargeLastSuccess = chargeLastSuccess;
    }

    public ZonedDateTime getChargeNextTime() {
        return chargeNextTime;
    }

    public Subscriber chargeNextTime(ZonedDateTime chargeNextTime) {
        this.chargeNextTime = chargeNextTime;
        return this;
    }

    public void setChargeNextTime(ZonedDateTime chargeNextTime) {
        this.chargeNextTime = chargeNextTime;
    }

    public Integer getTrialCount() {
        return trialCount;
    }

    public void setTrialCount(Integer trialCount) {
        this.trialCount = trialCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public ZonedDateTime getNotifyLastTime() {
        return notifyLastTime;
    }

    public void setNotifyLastTime(ZonedDateTime notifyLastTime) {
        this.notifyLastTime = notifyLastTime;
    }

    public Subscriber notifyLastTime(ZonedDateTime notifyLastTime) {
        this.notifyLastTime = notifyLastTime;
        return this;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }

    public Subscriber expiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
        return this;
    }

    public void setExpiryTime(ZonedDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getTag() {
        return tag;
    }

    public Subscriber tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getExtraData() {
        return extraData;
    }

    public Subscriber extraData(String extraData) {
        this.extraData = extraData;
        return this;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    public Integer getNotify() {
        return notify;
    }

    public void setNotify(Integer notify) {
        this.notify = notify;
    }

    public Subscriber notify(Integer notify) {
        this.notify = notify;
        return this;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getRegChannel() {
        return regChannel;
    }

    public void setRegChannel(String regChannel) {
        this.regChannel = regChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscriber subMsisdn = (Subscriber) o;
        if (subMsisdn.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subMsisdn.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubMsisdn{"
                + "id=" + getId()
                + ", msisdn='" + getMsisdn() + "'"
                + ", status='" + getStatus() + "'"
                + ", wapPassword='" + getWapPassword() + "'"
                + ", productId='" + getProductId() + "'"
                + ", partnerCode='" + getPartnerCode() + "'"
                + ", joinAt='" + getJoinAt() + "'"
                + ", joinChannel='" + getJoinChannel() + "'"
                + ", leftAt='" + getLeftAt() + "'"
                + ", leftChannel='" + getLeftChannel() + "'"
                + ", chargeLastTime='" + getChargeLastTime() + "'"
                + ", chargeLastSuccess='" + getChargeLastSuccess() + "'"
                + ", chargeNextTime='" + getChargeNextTime() + "'"
                + ", expiryTime='" + getExpiryTime() + "'"
                + ", tag='" + getTag() + "'"
                + ", extraData='" + getExtraData() + "'"
                + "}";
    }
}
