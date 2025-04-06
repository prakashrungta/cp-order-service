package com.restaurant.orders.entities;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Entity representing an order.
 */
@Table(name = "orders")
@Getter
@Setter
@Schema(description = "Entity representing an order")
public class Order {

    /**
     * Unique identifier for the order.
     */
    @Id
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