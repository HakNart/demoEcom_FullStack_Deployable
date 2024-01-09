package com.kt.rest.demoEcommerce.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.rest.demoEcommerce.model.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "_order")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}