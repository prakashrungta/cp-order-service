package com.restaurant.orders.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entity representing an order.
 */
@Entity(name = "orders")
@Getter
@Setter
@Schema(description = "Entity representing an order")
public class Order {

    /**
     * Unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the order", example = "1", accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    private Long id;

    /**
     * Description of the order.
     */
    @Schema(description = "Description of the order", example = "Pizza Margherita", nullable = true)
    @Size(max = 255, message = "Description should not exceed 255 characters")
    private String description;

    /**
     * Price of the order.
     */
    @Schema(description = "Price of the order", example = "12.50", nullable = true)
    @Positive(message = "Price must be positive")
    private Double price;

}