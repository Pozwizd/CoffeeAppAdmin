package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.Orders;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Orders}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDto implements Serializable {
    Long id;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime dateTimeOfCreate;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime dateTimeOfUpdate;
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    LocalDateTime dateTimeOfReady;

    DeliveryDto delivery;
    List<OrderItemDto> orderItemsDto;

    Orders.Payment payment;
    Orders.OrderStatus status;
}