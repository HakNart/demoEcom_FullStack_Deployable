package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderRequestDTO;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderResponseDTO;
import com.kt.rest.demoEcommerce.model.dto.OrderDTO;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.service.OrderService;
import com.kt.rest.demoEcommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {


    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {

        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(Authentication authentication, @RequestBody CreateOrderRequestDTO createdOrderRequest) {
        // Verify authentication
        var requester = userService.findUserByUserName(authentication.getName());
        CreateOrderResponseDTO orderResponseDTO = orderService.createOrder(requester, createdOrderRequest);
        ApiResponse<Object> response = ApiResponse.builder().success(orderResponseDTO).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOneOrder(Authentication authentication, @PathVariable("id") Long id) {
        var requester = userService.findUserByUserName(authentication.getName());
        Order order = orderService.findOneOrderById(id);
        OrderDTO orderDTO = OrderDTO.fromOrder(order);

        var apiResponse = ApiResponse.builder().success(orderDTO).build();
        return ResponseEntity.ok(apiResponse);
    }

}
