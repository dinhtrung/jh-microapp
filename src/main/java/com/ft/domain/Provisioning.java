package com.ft.domain;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Dec 1, 2017, 7:56:40 PM
 * @Quote To code is human, to debug is coffee
 */
@Document(collection = "provisioning")
@CompoundIndexes({
    @CompoundIndex(name = "subscriber_product_content_date",
            unique = true, def = "{'msisdn' : 1, 'product_id' : 1, 'content_id' : 1, 'date_logged': 1}")
})
public class Provisioning implements Serializable {

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

    /**
     * Product Associated
     */
    @Size(max = 40)
    @ApiModelProperty(value = "Product Associated")
    @Field("product_id")
    @Indexed
    private String productId;

    /**
     * Product Associated
     */
    @Size(max = 40)
    @ApiModelProperty(value = "Content Associated")
    @Field("content_id")
    @Indexed
    private String contentId;

    /**
     * Product Associated
     */
    @Size(max = 40)
    @ApiModelProperty(value = "Daily status of the subscription: fresh, fulfilled, failed, pending")
    @Field("status")
    @Indexed
    private String status;

    @Field("trail_count")
    @Indexed
    private Integer trialCount;

    @Field("success_average")
    @Indexed
    private Double successAverage;

    /**
     * Leaving information
     */
    @ApiModelProperty(value = "The date the subscription was logged and to be fulfilled.")
    @Field("date_logged")
    @Indexed
    private Date dateLogged;

    /**
     * Leaving information
     */
    @ApiModelProperty(value = "If billing fails, when next should we try again?")
    @Field("next_retrial")
    @Indexed
    private Date nextRetrial;

    @Field("notify_before_billing")
    @Indexed
    private Boolean notifyBeforeBilling;

    @Field("notify_on_failed_billing")
    @Indexed
    private Boolean notifyOnFailedBilling;

    @Field("bill_per_sms")
    @Indexed
    private Boolean billPerSms;

    @Field("amount_charged")
    @Indexed
    private Double amountCharged;

    public Provisioning() {

    }

    public Provisioning(String msisdn, String productId, String contentId, String status, Integer trialCount,
            Date dateLogged) {
        this.msisdn = msisdn;
        this.productId = productId;
        this.contentId = contentId;
        this.status = status;
        this.trialCount = trialCount;
        this.dateLogged = dateLogged;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTrialCount() {
        return trialCount;
    }

    public void setTrialCount(Integer trialCount) {
        this.trialCount = trialCount;
    }

    public Double getSuccessAverage() {
        return successAverage;
    }

    public void setSuccessAverage(Double successAverage) {
        this.successAverage = successAverage;
    }

    public Date getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(Date dateLogged) {
        this.dateLogged = dateLogged;
    }

    public Date getNextRetrial() {
        return nextRetrial;
    }

    public void setNextRetrial(Date nextRetrial) {
        this.nextRetrial = nextRetrial;
    }

    public Boolean getNotifyBeforeBilling() {
        return notifyBeforeBilling;
    }

    public void setNotifyBeforeBilling(Boolean notifyBeforeBilling) {
        this.notifyBeforeBilling = notifyBeforeBilling;
    }

    public Boolean getNotifyOnFailedBilling() {
        return notifyOnFailedBilling;
    }

    public void setNotifyOnFailedBilling(Boolean notifyOnFailedBilling) {
        this.notifyOnFailedBilling = notifyOnFailedBilling;
    }

    public Boolean getBillPerSms() {
        return billPerSms;
    }

    public void setBillPerSms(Boolean billPerSms) {
        this.billPerSms = billPerSms;
    }

    public Double getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(Double amountCharged) {
        this.amountCharged = amountCharged;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Provisioning other = (Provisioning) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Subscription{" + "id=" + id + ", msisdn=" + msisdn + ", productId=" + productId + ", contentId=" + contentId + ", status=" + status + ", trialCount=" + trialCount + ", dateLogged=" + dateLogged + '}';
    }

}
