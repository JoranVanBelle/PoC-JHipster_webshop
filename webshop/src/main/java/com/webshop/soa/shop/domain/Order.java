package com.webshop.soa.shop.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Order.
 */
@Table("jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("order_id")
    private Long orderID;

    @NotNull(message = "must not be null")
    @Column("order_price")
    private Double orderPrice;

    @NotNull(message = "must not be null")
    @Column("order_quantity")
    private Long orderQuantity;

    @NotNull(message = "must not be null")
    @Column("username")
    private String username;

    @NotNull(message = "must not be null")
    @Column("transport_id")
    private Long transportID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderID() {
        return this.orderID;
    }

    public Order orderID(Long orderID) {
        this.setOrderID(orderID);
        return this;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Double getOrderPrice() {
        return this.orderPrice;
    }

    public Order orderPrice(Double orderPrice) {
        this.setOrderPrice(orderPrice);
        return this;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getOrderQuantity() {
        return this.orderQuantity;
    }

    public Order orderQuantity(Long orderQuantity) {
        this.setOrderQuantity(orderQuantity);
        return this;
    }

    public void setOrderQuantity(Long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getUsername() {
        return this.username;
    }

    public Order username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTransportID() {
        return this.transportID;
    }

    public Order transportID(Long transportID) {
        this.setTransportID(transportID);
        return this;
    }

    public void setTransportID(Long transportID) {
        this.transportID = transportID;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderID=" + getOrderID() +
            ", orderPrice=" + getOrderPrice() +
            ", orderQuantity=" + getOrderQuantity() +
            ", username='" + getUsername() + "'" +
            ", transportID=" + getTransportID() +
            "}";
    }
}
