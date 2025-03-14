package com.restaurant.orders.controllers;

import com.restaurant.orders.entities.Order;
import com.restaurant.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }
}