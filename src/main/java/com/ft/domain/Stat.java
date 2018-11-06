package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Stat.
 */
@Document(collection = "stat")
public class Stat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Field("start_date")
    private LocalDate startDate;

    @NotNull
    @Field("end_date")
    private LocalDate endDate;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove

    @Field("data")
    private Object data;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Stat startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Stat endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Object getData() {
        return data;
    }

    public Stat data(Object data) {
        this.data = data;
        return this;
    }

    public void setData(Object data) {
        this.data = data;
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
        Stat stat = (Stat) o;
        if (stat.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stat.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stat{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
