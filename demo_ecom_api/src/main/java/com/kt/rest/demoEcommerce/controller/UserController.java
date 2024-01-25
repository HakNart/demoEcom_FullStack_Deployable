package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.dto.OrderHistoryDTO;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.BusinessService;
import com.kt.rest.demoEcommerce.service.OrderService;
import com.kt.rest.demoEcommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;


    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    //  Return user's basic information
    @GetMapping("/self")
    public ResponseEntity<?> getSelfUser(Authentication authentication) {
        User user = userService.findUserByUserName(authentication.getName());

        UserDetailResponse userDetailResponse = userService.mapUserToUserDetailResponse(user);

        ApiResponse apiResponse = ApiResponse.builder().success(userDetailResponse).build();

        return ResponseEntity.ok(apiResponse);
    }

    // Return user's order history
    @GetMapping("/self/orders")
    public ResponseEntity<?> getUserOrderHistory(Authentication authentication) {
        User user = userService.findUserByUserName(authentication.getName());
        List<Order> orderList = orderService.findAllOrdersByUserId(user.getId());

        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
        orderList.forEach(order -> {
            orderHistoryDTOList.add(orderService.mapOrderToOrderHistoryDTO(order));
        });
        ApiResponse apiResponse = ApiResponse.builder().success(orderHistoryDTOList).build();
        return ResponseEntity.ok(apiResponse);
    }
}
