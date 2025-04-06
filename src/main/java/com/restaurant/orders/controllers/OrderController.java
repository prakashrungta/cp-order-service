package com.restaurant.orders.controllers;

import com.restaurant.orders.entities.Order;
import com.restaurant.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Controller", description = "API Endpoint for managing orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructor for OrderController.
     *
     * @param orderService the service to manage orders
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Creates a new order.
     *
     * @param order the order to create
     * @return the created order
     */
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Creates a new order", description = "Creates a new order with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<Order> createOrder(@Valid  @RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    /**
     * Retrieves all orders.
     *
     * @return a list of all orders
     */
    @GetMapping
    @PreAuthorize("hasRole('user') or hasRole('admin')")
    @Operation(summary = "Gets all orders", description = "Retrieves a list of all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Flux<Order> getOrders() {
        return orderService.getAllOrders();
    }
}