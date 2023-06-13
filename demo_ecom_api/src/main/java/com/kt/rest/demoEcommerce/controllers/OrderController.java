package com.kt.rest.demoEcommerce.controllers;

import com.kt.rest.demoEcommerce.controllers.exeptions.UserNotFoundException;
import com.kt.rest.demoEcommerce.models.auth.User;
import com.kt.rest.demoEcommerce.models.shopEntities.Order;
import com.kt.rest.demoEcommerce.models.dataModels.OrderCreateRequest;
import com.kt.rest.demoEcommerce.models.dataModels.OrderDetailResponse;
import com.kt.rest.demoEcommerce.models.dataModels.UserDetailResponse;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.services.BusinessService;
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
            throw new UserNotFoundException("User not found");
        }
        User user = getUser.get();
        Order order = new Order (
                user,
                createdOrderRequest.getAmountPaid(),
                createdOrderRequest.getQuantity(),
                createdOrderRequest.getOrderItems()
        );
        orderRepository.save(order);
        UserDetailResponse userDetailResponse = businessService.createUserDetailResponse(user);
        OrderDetailResponse orderDetailResponse = businessService.createOrderDetailResponse(order, userDetailResponse);
        return ResponseEntity.ok(orderDetailResponse);

    }

}
