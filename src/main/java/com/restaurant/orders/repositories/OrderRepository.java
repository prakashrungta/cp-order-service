package com.restaurant.orders.repositories;

import com.restaurant.orders.entities.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Order entities.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}