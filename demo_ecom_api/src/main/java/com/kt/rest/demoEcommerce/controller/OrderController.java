package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.dto.OrderCreateRequest;
import com.kt.rest.demoEcommerce.model.dto.OrderDetailResponse;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final BusinessService businessService;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository, BusinessService businessService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrder(@RequestBody OrderCreateRequest createdOrderRequest) {
        Optional<User> getUser = userRepository.findById(createdOrderRequest.getUserId());
        if(!getUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = getUser.get();

        Order order = Order.builder().user(user)
                        .amountPaid(createdOrderRequest.getAmountPaid())
                                .quantity(createdOrderRequest.getQuantity())
                                        .orderItems(createdOrderRequest.getOrderItems()).build();
        orderRepository.save(order);
        UserDetailResponse userDetailResponse = businessService.createUserDetailResponse(user);
        OrderDetailResponse orderDetailResponse = businessService.createOrderDetailResponse(order, userDetailResponse);
        return ResponseEntity.ok(orderDetailResponse);

    }

}
