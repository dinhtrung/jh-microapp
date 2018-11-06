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
 * A CdrStat.
 */
@Document(collection = "cdr_stat")
public class CdrStat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    /** Store statistic date time in format YYYY-MM-DDTHH on UTC hour **/
    @Field("dateTime")
    @NotNull
    @Indexed
    private String dateTime;

    // Search function facilities
    private ZonedDateTime dateFrom;
    private ZonedDateTime dateTo;

	@Field("cnt")
    private Long cnt;

    @Field("revenue")
    private Double revenue;

    @Field("ref_type")
    private String refType;
    public static final String TYPE_PRODUCT="product";

    @Field("ref_id")
    private String refId;


    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
    	if(id==null) {
    		id = refId + "/" + dateTime;
    	}
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
		return dateTime;
	}

    public CdrStat dateTime(String dateTime) {
    	this.dateTime = dateTime;
    	return this;
    }

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public ZonedDateTime getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(ZonedDateTime dateFrom) {
		this.dateFrom = dateFrom;
	}

	public ZonedDateTime getDateTo() {
		return dateTo;
	}

	public void setDateTo(ZonedDateTime dateTo) {
		this.dateTo = dateTo;
	}

	public Long getCnt() {
        return cnt;
    }

    public CdrStat cnt(Long cnt) {
        this.cnt = cnt;
        return this;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }

    public Double getRevenue() {
        return revenue;
    }

    public CdrStat revenue(Double revenue) {
        this.revenue = revenue;
        return this;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public String getRefType() {
        return refType;
    }

    public CdrStat refType(String refType) {
        this.refType = refType;
        return this;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getRefId() {
        return refId;
    }

    public CdrStat refId(String refId) {
        this.refId = refId;
        return this;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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
        CdrStat cdrStat = (CdrStat) o;
        if (cdrStat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cdrStat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

	@Override
	public String toString() {
		return "CdrStat [id=" + id + ", dateTime=" + dateTime + ", cnt=" + cnt + ", revenue=" + revenue + ", refType="
				+ refType + ", refId=" + refId + "]";
	}



}
