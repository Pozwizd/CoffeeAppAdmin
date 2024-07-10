package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.OrderItem;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link OrderItem}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto implements Serializable {
    Long id;
    Long productId;
    String productName;
    int quantity;
}