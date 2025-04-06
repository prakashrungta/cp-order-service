package com.restaurant.orders.service;

import com.restaurant.orders.entities.Order;
import com.restaurant.orders.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        Flux<Order> orders = Flux.just(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        Flux<Order> result = orderService.getAllOrders();

        // Assert
        List<Order> resultList = result.collectList().block();
        assertEquals(2, resultList.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testSaveOrder() {
        // Arrange
        Order order = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(Mono.just(order));

        // Act
        Mono<Order> result = orderService.saveOrder(order);

        // Assert
        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }
}