package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Delivery;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    Long cityId;
    String street;
    String building;
    String subDoor;
    String apartment;
    LocalDate deliveryDate;
    LocalTime deliveryTime;
    Double changeAmount;
    Delivery.DeliveryStatus status;
}