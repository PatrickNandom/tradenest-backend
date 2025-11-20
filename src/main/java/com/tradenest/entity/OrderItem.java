package com.tradenest.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Order item must reference a product")
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull(message = "Order item must belong to an order")
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private int quantity;

    @NotNull(message = "Price at purchase is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price at purchase must be positive")
    @Column(nullable = false)
    private BigDecimal priceAtPurchase;
}
