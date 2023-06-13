package com.kt.rest.demoEcommerce.models.shopEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.rest.demoEcommerce.models.auth.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    private Integer quantity;


    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name="order_product",
            joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    @JsonProperty("cartList")
    private Set<Product> orderItems;
    // Constructors, getters, and setters

    public Order() {
    }

    public Order(Long id, User user, BigDecimal amountPaid, Integer quantity, Set<Product> orderItems) {
        this.id = id;
        this.user = user;
        this.amountPaid = amountPaid;
        this.quantity = quantity;
        this.orderItems = orderItems;
    }

    public Order(User user, BigDecimal amountPaid, Integer quantity, Set<Product> orderItems) {
        this.user = user;
        this.amountPaid = amountPaid;
        this.quantity = quantity;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<Product> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<Product> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", amountPaid=" + amountPaid +
                ", quantity=" + quantity +
                ", orderItems=" + orderItems +
                '}';
    }
}