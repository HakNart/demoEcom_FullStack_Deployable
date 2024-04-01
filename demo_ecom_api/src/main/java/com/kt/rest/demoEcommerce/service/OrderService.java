package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderRequestDTO;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderResponseDTO;
import com.kt.rest.demoEcommerce.model.dto.OrderHistoryDTO;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Transactional
    public CreateOrderResponseDTO createOrder(User requester, CreateOrderRequestDTO request) {
        Order order = Order.builder()
                .user(requester)
                .amountPaid(request.getAmountPaid())
                .quantity(request.getQuantity())
                .orderItems(request.getCartItems())
                .build();

        orderRepository.save(order);
        log.info("Order id {} created", order.getId());
        UserDetailResponse userDetailResponse = userService.mapUserToUserDetailResponse(requester);
        CreateOrderResponseDTO responseDTO = generateCreateOrderResponse(order, userDetailResponse);
        return responseDTO;
    }

    @Transactional
    public List<Order> findAllOrdersByUserId(Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }

    private CreateOrderResponseDTO generateCreateOrderResponse(Order order, UserDetailResponse userDetailResponse) {
        return CreateOrderResponseDTO.builder()
                .userDetail(userDetailResponse)
                .amountPaid(order.getAmountPaid())
                .quantity(order.getQuantity())
                .id(order.getId())
                .build();
    }

    public OrderHistoryDTO mapOrderToOrderHistoryDTO(Order order) {
        return OrderHistoryDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .amountPaid(order.getAmountPaid())
                .quantity(order.getQuantity())
                .orderItems(order.getOrderItems())
                .build();
    }

    public Order findOneOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Order", "order id", id));
    }
}
