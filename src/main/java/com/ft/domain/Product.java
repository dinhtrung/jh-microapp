package com.ft.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.ft.service.dto.ChargingProfile;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * It also related to the JoinChannel of the customer
 */
@ApiModel(description = "It also related to the JoinChannel of the customer")
@Document(collection = "sub_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Size(max = 80)
    @Field("code")
    private String code;

    @Field("description")
    private String description;

    @ApiModelProperty(value = "ROTATIONAL or NON_ROTATIONAL")
    @Field("content_type")
    @Indexed
    private String contentType;

    @Min(value = 1)
    @ApiModelProperty(value = "How many are we selling per day at the moment")
    @Field("content_per_day")
    @Indexed
    private Integer contentPerDay;

    /**
     * Enable, disabled
     */
    @ApiModelProperty(value = "Enable, disabled")
    @Field("status")
    private Boolean status;

    /**
     * The channel to broadcast this particular product. Can be WAP / SMS / IVR
     * / OBD etc
     */
    @Size(max = 20)
    @ApiModelProperty(value = "The channel to broadcast this particular product. Can be WAP / SMS / IVR / OBD etc")
    @Field("join_channel")
    private String joinChannel;

    /**
     * Used as identifier for this particular Product. SMS: code should be the
     * short code that receive MO. WAP: code should be the URL used for
     * broadcasting. Can specify a pattern to check on the data pass into
     * integration channel.
     */
    @ApiModelProperty(value = "Used as identifier for this particular SubProduct. SMS: code should be the short code that receive MO. WAP: code should be the URL used for broadcasting. Can specify a pattern to check on the data pass into integration channel.")
    @Field("join_pattern")
    private List<String> joinPattern = new ArrayList<>();

    /**
     * Used as identifier for this particular Product.
     */
    @ApiModelProperty(value = "Used as identifier for this particular SubProduct.")
    @Field("left_pattern")
    private List<String> leftPattern = new ArrayList<>();

    /**
     * Message to send back to the client Successful opt-in
     */
    @ApiModelProperty(value = "Message to send back to the client Successful opt-in")
    @Field("msg_welcome")
    private String msgWelcome;

    /**
     * Message to send back to the client Successful opt-in
     */
    @ApiModelProperty(value = "Message to send back to the client asking for double confirmation")
    @Field("msg_confirm")
    private String msgConfirm;
    
    @ApiModelProperty(value = "Message to send back for subscribers to confirm subscription. This introduced for uniformity with other apps.")
    @Field("double_optin_msg")
    private String doubleOptinMsg;

    /**
     * Message to send back to the client Successful opt-in
     */
    @ApiModelProperty(value = "Message to send back to the client Successful opt-in")
    @Field("msg_renew")
    private String msgRenew;

    /**
     * Message to send back to the client Successful opt-in
     */
    @ApiModelProperty(value = "Message to send back to the client before charge")
    @Field("msg_precharge")
    private String msgPrecharge;

    /**
     * Message to send back to the client Successful opt-in
     */
    @ApiModelProperty(value = "Message to send back to the client when all possible charges are failed")
    @Field("msg_charge_failed")
    private String msgChargeFailed;

    /**
     * SUccessful opt-out
     */
    @ApiModelProperty(value = "SUccessful opt-out")
    @Field("msg_farewell")
    private String msgFarewell;

    /**
     * Error when customer already joined
     */
    @ApiModelProperty(value = "Error when customer already joined")
    @Field("msg_already_joined")
    private String msgAlreadyJoined;

    /**
     * Error when customer already left
     */
    @ApiModelProperty(value = "Error when customer already left")
    @Field("msg_already_left")
    private String msgAlreadyLeft;

    /**
     * Error when customer not used this services before
     */
    @ApiModelProperty(value = "Error when customer not used this services before")
    @Field("msg_not_used")
    private String msgNotUsed;

    /**
     * How many days will we do the charge again
     */
    @ApiModelProperty(value = "How to charge our customers")
    @Field("charging_profiles")
    private List<ChargingProfile> chargingProfiles = new ArrayList<>();

    /**
     * Assigned for partner
     */
    @Size(max = 40)
    @ApiModelProperty(value = "Assigned for partner")
    @Field("partner_code")
    private String partnerCode;

    @NotNull
    @Field("broadcast_hours")
    private String broadcastHours;

    @Field("broadcast_weekday")
    private String broadcastWeekday;

    @NotNull
    @Field("broadcast_shortcode")
    private String broadcastShortcode;
    
    @Field("telco_service_code")
    private String telcoServiceCode;
    
    @Field("price")
    private Double price;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoubleOptinMsg() {
        return doubleOptinMsg;
    }

    public void setDoubleOptinMsg(String doubleOptinMsg) {
        this.doubleOptinMsg = doubleOptinMsg;
    }

    public String getCode() {
        return code;
    }

    public Product code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isStatus() {
        return status;
    }

    public Boolean getStatus() {
        return status;
    }

    public Product status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getJoinChannel() {
        return joinChannel;
    }

    public Product joinChannel(String joinChannel) {
        this.joinChannel = joinChannel;
        return this;
    }

    public void setJoinChannel(String joinChannel) {
        this.joinChannel = joinChannel;
    }

    public List<String> getJoinPattern() {
        return joinPattern;
    }

    public Product joinPattern(List<String> joinPattern) {
        this.joinPattern = joinPattern;
        return this;
    }

    public void setJoinPattern(List<String> joinPattern) {
        this.joinPattern = joinPattern;
    }

    public List<String> getLeftPattern() {
        return leftPattern;
    }

    public Product leftPattern(List<String> leftPattern) {
        this.leftPattern = leftPattern;
        return this;
    }

    public void setLeftPattern(List<String> leftPattern) {
        this.leftPattern = leftPattern;
    }

    public String getMsgWelcome() {
        return msgWelcome;
    }

    public Product msgWelcome(String msgWelcome) {
        this.msgWelcome = msgWelcome;
        return this;
    }

    public void setMsgWelcome(String msgWelcome) {
        this.msgWelcome = msgWelcome;
    }

    public String getMsgFarewell() {
        return msgFarewell;
    }

    public Product msgFarewell(String msgFarewell) {
        this.msgFarewell = msgFarewell;
        return this;
    }

    public void setMsgFarewell(String msgFarewell) {
        this.msgFarewell = msgFarewell;
    }

    public String getMsgAlreadyJoined() {
        return msgAlreadyJoined;
    }

    public Product msgAlreadyJoined(String msgAlreadyJoined) {
        this.msgAlreadyJoined = msgAlreadyJoined;
        return this;
    }

    public void setMsgAlreadyJoined(String msgAlreadyJoined) {
        this.msgAlreadyJoined = msgAlreadyJoined;
    }

    public String getMsgAlreadyLeft() {
        return msgAlreadyLeft;
    }

    public Product msgAlreadyLeft(String msgAlreadyLeft) {
        this.msgAlreadyLeft = msgAlreadyLeft;
        return this;
    }

    public void setMsgAlreadyLeft(String msgAlreadyLeft) {
        this.msgAlreadyLeft = msgAlreadyLeft;
    }

    public String getMsgNotUsed() {
        return msgNotUsed;
    }

    public Product msgNotUsed(String msgNotUsed) {
        this.msgNotUsed = msgNotUsed;
        return this;
    }

    public void setMsgNotUsed(String msgNotUsed) {
        this.msgNotUsed = msgNotUsed;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public Product partnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
        return this;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getBroadcastHours() {
        return broadcastHours;
    }

    public Product broadcastHours(String broadcastHours) {
        this.broadcastHours = broadcastHours;
        return this;
    }

    public void setBroadcastHours(String broadcastHours) {
        this.broadcastHours = broadcastHours;
    }

    public String getBroadcastWeekday() {
        return broadcastWeekday;
    }

    public Product broadcastWeekday(String broadcastWeekday) {
        this.broadcastWeekday = broadcastWeekday;
        return this;
    }

    public void setBroadcastWeekday(String broadcastWeekday) {
        this.broadcastWeekday = broadcastWeekday;
    }

    public String getBroadcastShortcode() {
        return broadcastShortcode;
    }

    public Product broadcastShortcode(String broadcastShortcode) {
        this.broadcastShortcode = broadcastShortcode;
        return this;
    }

    public void setBroadcastShortcode(String broadcastShortcode) {
        this.broadcastShortcode = broadcastShortcode;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    public List<ChargingProfile> getChargingProfiles() {
        return chargingProfiles;
    }

    public void setChargingProfiles(List<ChargingProfile> chargingProfiles) {
        this.chargingProfiles = chargingProfiles;
    }

    public String getMsgConfirm() {
        return msgConfirm;
    }

    public void setMsgConfirm(String msgConfirm) {
        this.msgConfirm = msgConfirm;
    }

    public String getMsgRenew() {
        return msgRenew;
    }

    public void setMsgRenew(String msgRenew) {
        this.msgRenew = msgRenew;
    }

    public String getMsgPrecharge() {
        return msgPrecharge;
    }

    public void setMsgPrecharge(String msgPrecharge) {
        this.msgPrecharge = msgPrecharge;
    }

    public String getMsgChargeFailed() {
        return msgChargeFailed;
    }

    public void setMsgChargeFailed(String msgChargeFailed) {
        this.msgChargeFailed = msgChargeFailed;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Integer getContentPerDay() {
        return contentPerDay;
    }

    public void setContentPerDay(Integer contentPerDay) {
        this.contentPerDay = contentPerDay;
    }

    public String getTelcoServiceCode() {
        return telcoServiceCode;
    }

    public void setTelcoServiceCode(String telcoServiceCode) {
        this.telcoServiceCode = telcoServiceCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product subProduct = (Product) o;
        if (subProduct.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subProduct.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubProduct{"
                + "id=" + getId()
                + ", code='" + getCode() + "'"
                + ", description='" + getDescription() + "'"
                + ", status='" + isStatus() + "'"
                + ", joinChannel='" + getJoinChannel() + "'"
                + ", joinPattern='" + getJoinPattern() + "'"
                + ", leftPattern='" + getLeftPattern() + "'"
                + ", msgWelcome='" + getMsgWelcome() + "'"
                + ", msgFarewell='" + getMsgFarewell() + "'"
                + ", msgAlreadyJoined='" + getMsgAlreadyJoined() + "'"
                + ", msgAlreadyLeft='" + getMsgAlreadyLeft() + "'"
                + ", msgNotUsed='" + getMsgNotUsed() + "'"
                + ", partnerCode='" + getPartnerCode() + "'"
                + ", broadcastHours='" + getBroadcastHours() + "'"
                + ", broadcastWeekday='" + getBroadcastWeekday() + "'"
                + ", broadcastShortcode='" + getBroadcastShortcode() + "'"
                + "}";
    }
}
