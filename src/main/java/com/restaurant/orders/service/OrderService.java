package com.restaurant.orders.service;

import com.restaurant.orders.RestClient.RestClient;
import com.restaurant.orders.entities.Order;
import com.restaurant.orders.repositories.OrderRepository;
import com.restaurant.orders.token.KeycloakTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service class for managing orders.
 * Provides methods to retrieve and save orders.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;


    private final RestClient restClient;


    private final KeycloakTokenService tokenService;

    /**
     * Constructs an OrderService with the specified OrderRepository.
     *
     * @param orderRepository the repository to manage orders
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, RestClient restClient, KeycloakTokenService tokenService) {
        this.orderRepository = orderRepository;
        this.restClient = restClient;
        this.tokenService = tokenService;
    }

    /**
     * Retrieves all orders from the repository.
     *
     * @return a list of all orders
     */
    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Saves the specified order to the repository.
     *
     * @param order the order to save
     * @return the saved order
     */
    public Mono<Order> saveOrder(Order order) {
        return restClient.callPaymentService(order.getId())
                .doOnSuccess(response -> System.out.println("Payment service called successfully for order ID: " + order.getId()))
                .then(orderRepository.save(order));

    }
}