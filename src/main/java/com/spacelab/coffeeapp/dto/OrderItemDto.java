package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.OrderItem;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link OrderItem}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto implements Serializable {
    Long id;
    Long CategoryId;
    Long productId;
    String productName;
    int quantity;
    List<OrderItemAttributeDto> attributes;
}