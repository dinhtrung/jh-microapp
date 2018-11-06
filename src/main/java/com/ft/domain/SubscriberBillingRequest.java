package com.ft.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Nov 25, 2017, 10:03:51 AM
 * @Quote To code is human, to debug is coffee
 */
@Document(collection = "subscriber_billing_request")
public class SubscriberBillingRequest implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Indexed
    @Field("request_date")
    private ZonedDateTime requestDate;
    @NotNull
    @Field("product_id")
    private String productId;
    @NotNull
    @Field("msisdn")
    private String msisdn;
    @NotNull
    @Field("content_id")
    private String contentId;
    @Field("amount_charged")
    private Double amountCharged;
    @Field("response_status")
    private String responseStatus;
    @Field("response_error_code")
    private String responseErrorCode;
    @Field("response_error_msg")
    private String responseErrorMsg;
    @Field("transaction_id")
    private String transactionId;
    @Field("cp_transaction_id")
    private String cpTransactionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Double getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(Double amountCharged) {
        this.amountCharged = amountCharged;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseErrorCode() {
        return responseErrorCode;
    }

    public void setResponseErrorCode(String responseErrorCode) {
        this.responseErrorCode = responseErrorCode;
    }

    public String getResponseErrorMsg() {
        return responseErrorMsg;
    }

    public void setResponseErrorMsg(String responseErrorMsg) {
        this.responseErrorMsg = responseErrorMsg;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCpTransactionId() {
        return cpTransactionId;
    }

    public void setCpTransactionId(String cpTransactionId) {
        this.cpTransactionId = cpTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final SubscriberBillingRequest other = (SubscriberBillingRequest) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "SubscriberBillingRequest{" + "id=" + id + ", requestDate=" + requestDate + ", productId=" + productId + ", msisdn=" + msisdn + ", contentId=" + contentId + ", amountCharged=" + amountCharged + ", responseStatus=" + responseStatus + ", responseErrorMsg=" + responseErrorMsg + ", transactionId=" + transactionId + ", cpTransactionId=" + cpTransactionId + '}';
    }
    
}



