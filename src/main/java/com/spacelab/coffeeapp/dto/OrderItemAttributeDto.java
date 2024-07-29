package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link OrderItemAttribute}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemAttributeDto implements Serializable {
    Long id;

    Long orderItemId;

    Long productAttributeId;

    Long attributeValueId;

}