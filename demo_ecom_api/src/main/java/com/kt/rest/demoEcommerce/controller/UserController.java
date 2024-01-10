package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.dto.OrderHistoryDetailResponse;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final BusinessService businessService;


    public UserController(UserRepository userRepository, OrderRepository orderRepository, BusinessService businessService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;

        this.businessService = businessService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUser(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
        return ResponseEntity.ok(userDetailResponse);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderHistoryDetailResponse>> getUserOrders(@RequestParam(value = "user.id", required =true) Long id) {
        List<Order> orderList = orderRepository.findAllByUserId(id);
        List<OrderHistoryDetailResponse> orderHistoryDetailResponseList = new ArrayList<>();
        orderList.forEach(order -> {
            orderHistoryDetailResponseList.add(businessService.createOrderHistoryDetailResponse(order));
        });
        return ResponseEntity.ok(orderHistoryDetailResponseList);
    }
}
