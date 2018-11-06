package com.ft.domain;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author Tunde Michael <tm@tundemichael.com>
 * @Date Apr 3, 2018, 1:04:28 PM
 * @Quote To code is human, to debug is coffee
 */
@Document(collection = "subscription")
public class Subscription implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;
    
    @NotNull
    @ApiModelProperty(value = "The ID of the owning subscriber.", required = true)
    @Field("subscriber_id")
    @Indexed
    private String subscriberId;
    
    @NotNull
    @ApiModelProperty(value = "Product Associated", required = true)
    @Field("product_id")
    @Indexed
    private String productId;

    @Field("product_desc")
    private String productDesc;
    @Field("sub_keyword")
    private String subKeyword;
    @Field("desub_keyword")
    private String deSubKeyword;
    @Field("status")
    private String status;
    @Field("sub_error_code")
    private String subErrorCode;
    @Field("sub_error_msg")
    private String subErrorMessage;
    @Field("desub_error_code")
    private String deSubErrorCode;
    @Field("desub_error_msg")
    private String deSubErrorMessage;

    @Size(max = 40)
    @ApiModelProperty(value = "SubCP Associated")
    @Field("partner_code")
    private String partnerCode;

    @ApiModelProperty(value = "subscription date")
    @Field("sub_date")
    private Date subscriptionDate;
    
    @ApiModelProperty(value = "activation date")
    @Field("activation_date")
    private Date activationDate;

    @Size(max = 40)
    @Field("sub_channel")
    private String subscriptionChannel;

    @ApiModelProperty(value = "Desubscription date")
    @Field("desub_date")
    private Date deSubscriptionDate;

    @Size(max = 40)
    @Field("desub_channel")
    private String deSubscriptionChannel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getSubKeyword() {
        return subKeyword;
    }

    public void setSubKeyword(String subKeyword) {
        this.subKeyword = subKeyword;
    }

    public String getDeSubKeyword() {
        return deSubKeyword;
    }

    public void setDeSubKeyword(String deSubKeyword) {
        this.deSubKeyword = deSubKeyword;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public String getSubscriptionChannel() {
        return subscriptionChannel;
    }

    public void setSubscriptionChannel(String subscriptionChannel) {
        this.subscriptionChannel = subscriptionChannel;
    }

    public Date getDeSubscriptionDate() {
        return deSubscriptionDate;
    }

    public void setDeSubscriptionDate(Date deSubscriptionDate) {
        this.deSubscriptionDate = deSubscriptionDate;
    }

    public String getDeSubscriptionChannel() {
        return deSubscriptionChannel;
    }

    public void setDeSubscriptionChannel(String deSubscriptionChannel) {
        this.deSubscriptionChannel = deSubscriptionChannel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubErrorCode() {
        return subErrorCode;
    }

    public void setSubErrorCode(String subErrorCode) {
        this.subErrorCode = subErrorCode;
    }

    public String getSubErrorMessage() {
        return subErrorMessage;
    }

    public void setSubErrorMessage(String subErrorMessage) {
        this.subErrorMessage = subErrorMessage;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public String getDeSubErrorCode() {
        return deSubErrorCode;
    }

    public void setDeSubErrorCode(String deSubErrorCode) {
        this.deSubErrorCode = deSubErrorCode;
    }

    public String getDeSubErrorMessage() {
        return deSubErrorMessage;
    }

    public void setDeSubErrorMessage(String deSubErrorMessage) {
        this.deSubErrorMessage = deSubErrorMessage;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.subscriberId);
        hash = 89 * hash + Objects.hashCode(this.productId);
        hash = 89 * hash + Objects.hashCode(this.productDesc);
        hash = 89 * hash + Objects.hashCode(this.subKeyword);
        hash = 89 * hash + Objects.hashCode(this.deSubKeyword);
        hash = 89 * hash + Objects.hashCode(this.partnerCode);
        hash = 89 * hash + Objects.hashCode(this.subscriptionDate);
        hash = 89 * hash + Objects.hashCode(this.subscriptionChannel);
        hash = 89 * hash + Objects.hashCode(this.deSubscriptionDate);
        hash = 89 * hash + Objects.hashCode(this.deSubscriptionChannel);
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
        final Subscription other = (Subscription) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.subscriberId, other.subscriberId)) {
            return false;
        }
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        if (!Objects.equals(this.productDesc, other.productDesc)) {
            return false;
        }
        if (!Objects.equals(this.subKeyword, other.subKeyword)) {
            return false;
        }
        if (!Objects.equals(this.deSubKeyword, other.deSubKeyword)) {
            return false;
        }
        if (!Objects.equals(this.partnerCode, other.partnerCode)) {
            return false;
        }
        if (!Objects.equals(this.subscriptionChannel, other.subscriptionChannel)) {
            return false;
        }
        if (!Objects.equals(this.deSubscriptionChannel, other.deSubscriptionChannel)) {
            return false;
        }
        if (!Objects.equals(this.subscriptionDate, other.subscriptionDate)) {
            return false;
        }
        return Objects.equals(this.deSubscriptionDate, other.deSubscriptionDate);
    }

    @Override
    public String toString() {
        return "Subscription{" + "id=" + id + ", subscriberId=" + subscriberId + ", productId=" + productId + ", productDesc=" + productDesc + ", subKeyword=" + subKeyword + ", deSubKeyword=" + deSubKeyword + ", status=" + status + ", subErrorCode=" + subErrorCode + ", subErrorMessage=" + subErrorMessage + ", deSubErrorCode=" + deSubErrorCode + ", deSubErrorMessage=" + deSubErrorMessage + ", partnerCode=" + partnerCode + ", subscriptionDate=" + subscriptionDate + ", subscriptionChannel=" + subscriptionChannel + ", deSubscriptionDate=" + deSubscriptionDate + ", deSubscriptionChannel=" + deSubscriptionChannel + '}';
    }

}
