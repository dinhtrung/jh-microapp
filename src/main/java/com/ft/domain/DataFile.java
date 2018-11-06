package com.ft.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DataFile.
 */
@Document(collection = "data_file")
public class DataFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @Field("data_file")
    private byte[] dataFile;

    @Field("data_file_content_type")
    private String dataFileContentType;

    @Field("created_at")
    private ZonedDateTime createdAt;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getDataFile() {
        return dataFile;
    }

    public DataFile dataFile(byte[] dataFile) {
        this.dataFile = dataFile;
        return this;
    }

    public void setDataFile(byte[] dataFile) {
        this.dataFile = dataFile;
    }

    public String getDataFileContentType() {
        return dataFileContentType;
    }

    public DataFile dataFileContentType(String dataFileContentType) {
        this.dataFileContentType = dataFileContentType;
        return this;
    }

    public void setDataFileContentType(String dataFileContentType) {
        this.dataFileContentType = dataFileContentType;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public DataFile createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
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
        DataFile dataFile = (DataFile) o;
        if (dataFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataFile{" +
            "id=" + getId() +
            ", dataFileContentType='" + dataFileContentType + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
