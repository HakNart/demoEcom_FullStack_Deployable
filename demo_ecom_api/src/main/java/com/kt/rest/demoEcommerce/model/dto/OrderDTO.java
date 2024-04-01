package com.kt.rest.demoEcommerce.model.dto;

import com.kt.rest.demoEcommerce.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Integer user_id;
    private BigDecimal amountPaid;
    private Integer quantity;
    private Set<Integer> orderItems;
    public static OrderDTO fromOrder(Order order) {
        Set<Integer> listOrders = order.getOrderItems().stream().map(o -> o.getId()).collect(Collectors.toSet());
        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getAmountPaid(),
                order.getQuantity(),
                listOrders
        );
    }
}
