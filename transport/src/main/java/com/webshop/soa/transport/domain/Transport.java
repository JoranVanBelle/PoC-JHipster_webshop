package com.webshop.soa.transport.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Transport.
 */
@Table("transport")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("transport_id")
    private Long transportID;

    @NotNull(message = "must not be null")
    @Column("transport_name")
    private String transportName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransportID() {
        return this.transportID;
    }

    public Transport transportID(Long transportID) {
        this.setTransportID(transportID);
        return this;
    }

    public void setTransportID(Long transportID) {
        this.transportID = transportID;
    }

    public String getTransportName() {
        return this.transportName;
    }

    public Transport transportName(String transportName) {
        this.setTransportName(transportName);
        return this;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transport)) {
            return false;
        }
        return id != null && id.equals(((Transport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transport{" +
            "id=" + getId() +
            ", transportID=" + getTransportID() +
            ", transportName='" + getTransportName() + "'" +
            "}";
    }
}
