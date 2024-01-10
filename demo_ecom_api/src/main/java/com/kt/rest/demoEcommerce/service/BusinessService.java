package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.model.dto.OrderDetailResponse;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.dto.OrderHistoryDetailResponse;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import com.kt.rest.demoEcommerce.repository.ProductRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private UserRepository userRepository;


    public BusinessService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }
    public UserDetailResponse createUserDetailResponse(User user) {

        return UserDetailResponse.builder()
                .name(user.getUsername())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
    public OrderHistoryDetailResponse createOrderHistoryDetailResponse(Order order) {

        return OrderHistoryDetailResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .amountPaid(order.getAmountPaid())
                .quantity(order.getQuantity())
                .orderItems(order.getOrderItems())
                .build();

    }
    public OrderDetailResponse createOrderDetailResponse(Order order, UserDetailResponse userDetailResponse) {
        return OrderDetailResponse.builder()
                .id(order.getId())
                .amountPaid(order.getAmountPaid())
                .quantity(order.getQuantity())
                .userDetail(userDetailResponse)
                .build();
    }
}
