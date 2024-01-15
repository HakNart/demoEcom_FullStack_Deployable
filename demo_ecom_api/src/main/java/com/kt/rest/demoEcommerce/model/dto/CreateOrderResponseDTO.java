package com.kt.rest.demoEcommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderResponseDTO {
    @JsonProperty("amount_paid")
    private BigDecimal amountPaid;
    private Integer quantity;
    private Long id;

    @JsonProperty("user")
    private UserDetailResponse userDetail;
}
