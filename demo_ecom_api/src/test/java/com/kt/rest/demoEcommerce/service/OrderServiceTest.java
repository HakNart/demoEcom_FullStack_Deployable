package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.config.AppUnitTest;
import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderRequestDTO;
import com.kt.rest.demoEcommerce.model.dto.CreateOrderResponseDTO;
import com.kt.rest.demoEcommerce.model.dto.OrderHistoryDTO;
import com.kt.rest.demoEcommerce.model.dto.UserDetailResponse;
import com.kt.rest.demoEcommerce.model.entity.Order;
import com.kt.rest.demoEcommerce.model.entity.Product;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AppUnitTest
class OrderServiceTest {
    @Mock private OrderRepository orderRepository;
    @Mock private UserService userService;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, userService);
    }

    @Test
    void testCreateOrder() {
        User requester = new User();
        requester.setId(1);
        CreateOrderRequestDTO request = CreateOrderRequestDTO.builder()
                .cartItems(Set.of(new Product()))
                .quantity(5)
                .amountPaid(BigDecimal.valueOf(10.3))
                .build();

        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                        .id(1).build();

        when(userService.mapUserToUserDetailResponse(requester)).thenReturn(userDetailResponse);

        CreateOrderResponseDTO responseDTO = orderService.createOrder(requester, request);

        assertNotNull(responseDTO);
        assertEquals(request.getAmountPaid(), responseDTO.getAmountPaid());
        assertEquals(request.getQuantity(), responseDTO.getQuantity());
        assertEquals(requester.getId(), responseDTO.getUserDetail().getId());
    }

    @Test
    void testFindAllOrdersByUserId() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        when(orderRepository.findAllByUserId(1)).thenReturn(orders);

        List<Order> result = orderService.findAllOrdersByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    void testMapOrderToOrderHistoryDTO() {
        Order order = Order.builder()
                .id(1L).user(User.builder().id(1).build()).quantity(5).amountPaid(BigDecimal.valueOf(15.5))
                .orderItems(Set.of(new Product())).build();

        OrderHistoryDTO historyDTO = orderService.mapOrderToOrderHistoryDTO(order);

        assertNotNull(historyDTO);
        assertEquals(1L, historyDTO.getId());
        assertEquals(1, historyDTO.getUserId());
        assertEquals(order.getAmountPaid(), historyDTO.getAmountPaid());
        assertEquals(order.getQuantity(), historyDTO.getQuantity());
        assertEquals(order.getOrderItems().size(), historyDTO.getOrderItems().size());
    }

    @Test
    void testFindOneOrderById_ExistingId() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findOneOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindOneOrderById_NonExistingId() {
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.findOneOrderById(100L));
    }

}