package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderResponseDTO;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.dto.OrderHistoryDTO;
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
                .username(user.getUsername())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
    public OrderHistoryDTO createOrderHistoryDetailResponse(Order order) {

        return OrderHistoryDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .amountPaid(order.getAmountPaid())
                .quantity(order.getQuantity())
                .orderItems(order.getOrderItems())
                .build();

    }
    public CreateOrderResponseDTO createOrderDetailResponse(Order order, UserDetailResponse userDetailResponse) {
        return CreateOrderResponseDTO.builder()
                .id(order.getId())
                .amountPaid(order.getAmountPaid())
                .quantity(order.getQuantity())
                .userDetail(userDetailResponse)
                .build();
    }
}
