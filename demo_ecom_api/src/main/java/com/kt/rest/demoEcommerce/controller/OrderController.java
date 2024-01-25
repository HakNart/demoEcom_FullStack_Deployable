package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderRequestDTO;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderResponseDTO;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.BusinessService;
import com.kt.rest.demoEcommerce.service.OrderService;
import com.kt.rest.demoEcommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {


    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {

        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(Authentication authentication, @RequestBody CreateOrderRequestDTO createdOrderRequest) {
        var requester = userService.findUserByUserName(authentication.getName());
        CreateOrderResponseDTO orderResponseDTO = orderService.createOrder(requester, createdOrderRequest);
        ApiResponse<Object> response = ApiResponse.builder().success(orderResponseDTO).build();
        return ResponseEntity.ok(response);
    }

}
