package com.kt.rest.demoEcommerce.model.dataModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kt.rest.demoEcommerce.model.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateRequest {
    @JsonProperty("cartList")
    private Set<Product> orderItems;
    private Integer userId;

    @JsonProperty("amount_paid")
    private BigDecimal amountPaid;
    private Integer quantity;

}
