package com.ft.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CdrStat entity.
 */
public class CdrStatDTO implements Serializable {

    private String id;

    @NotNull
    private LocalDate statDate;

    @NotNull
    @Min(value = 0)
    @Max(value = 23)
    private Integer statHour;

    private Long cnt;

    private Double revenue;

    private String productId;

    private String refType;

    private String refId;

    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
    }

    public Integer getStatHour() {
        return statHour;
    }

    public void setStatHour(Integer statHour) {
        this.statHour = statHour;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CdrStatDTO cdrStatDTO = (CdrStatDTO) o;
        if(cdrStatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cdrStatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CdrStatDTO{" +
            "id=" + getId() +
            ", statDate='" + getStatDate() + "'" +
            ", statHour='" + getStatHour() + "'" +
            ", cnt='" + getCnt() + "'" +
            ", revenue='" + getRevenue() + "'" +
            ", productId='" + getProductId() + "'" +
            ", refType='" + getRefType() + "'" +
            ", refId='" + getRefId() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
