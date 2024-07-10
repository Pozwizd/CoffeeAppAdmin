package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Delivery;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Delivery}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto implements Serializable {
    Long id;
    String name;
    String phoneNumber;
    String street;
    String building;
    String apartment;
    LocalDateTime deliveryTime;
    LocalDateTime actualDeliveryTime;
    Double changeAmount;
    Delivery.DeliveryStatus status;
}