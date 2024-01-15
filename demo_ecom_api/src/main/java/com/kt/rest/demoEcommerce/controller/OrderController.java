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
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final BusinessService businessService;
    private final UserService userService;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository, BusinessService businessService, UserService userService, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.businessService = businessService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(Authentication authentication, @RequestBody CreateOrderRequestDTO createdOrderRequest) {
//        Optional<User> getUser = userRepository.findById(createdOrderRequest.getUserId());
//        if(!getUser.isPresent()) {
//            throw new ResourceNotFoundException("User not found");
//        }
//        User user = getUser.get();
//
//        Order order = Order.builder().user(user)
//                        .amountPaid(createdOrderRequest.getAmountPaid())
//                                .quantity(createdOrderRequest.getQuantity())
//                                        .orderItems(createdOrderRequest.getCartItems()).build();
//        orderRepository.save(order);
//        UserDetailResponse userDetailResponse = businessService.createUserDetailResponse(user);
//        CreateOrderResponseDTO createOrderResponseDTO = businessService.createOrderDetailResponse(order, userDetailResponse);
//        return ResponseEntity.ok(createOrderResponseDTO);
        var requester = userService.findUserByUserName(authentication.getName());
        CreateOrderResponseDTO orderResponseDTO = orderService.createOrder(requester, createdOrderRequest);
        ApiResponse<Object> response = ApiResponse.builder().success(orderResponseDTO).build();
        return ResponseEntity.ok(response);
    }


}
