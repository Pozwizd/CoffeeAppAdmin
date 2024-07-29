package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Order}
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

    DeliveryDto deliveryDto;
    List<OrderItemDto> orderItemsDto;

    Order.Payment payment;
    Order.OrderStatus status;

    double totalAmount = 0;
}