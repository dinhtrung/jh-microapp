package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Dnd.
 */
@Document(collection = "dnd")
public class Dnd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("join_at")
    private ZonedDateTime joinAt;

    @Size(max = 40)
    @Field("join_channel")
    private String joinChannel;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getJoinAt() {
        return joinAt;
    }

    public Dnd joinAt(ZonedDateTime joinAt) {
        this.joinAt = joinAt;
        return this;
    }

    public void setJoinAt(ZonedDateTime joinAt) {
        this.joinAt = joinAt;
    }

    public String getJoinChannel() {
        return joinChannel;
    }

    public Dnd joinChannel(String joinChannel) {
        this.joinChannel = joinChannel;
        return this;
    }

    public void setJoinChannel(String joinChannel) {
        this.joinChannel = joinChannel;
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
        Dnd dnd = (Dnd) o;
        if (dnd.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dnd.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Dnd{" +
            "id=" + getId() +
            ", joinAt='" + getJoinAt() + "'" +
            ", joinChannel='" + getJoinChannel() + "'" +
            "}";
    }
}
