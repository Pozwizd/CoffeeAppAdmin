package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.DeliveryDto;
import com.spacelab.coffeeapp.dto.OrderItemDto;
import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderMapper {

    public OrdersDto toDto(Orders orders) {
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(orders.getId());
        ordersDto.setDateTimeOfCreate(orders.getDateTimeOfCreate());
        ordersDto.setDateTimeOfUpdate(orders.getDateTimeOfUpdate());
        ordersDto.setDateTimeOfReady(orders.getDateTimeOfReady());
        ordersDto.setStatus(orders.getStatus());
        ordersDto.setPayment(orders.getPayment());
        Delivery delivery = orders.getDelivery();
        if (delivery != null) {
            DeliveryDto deliveryDto = new DeliveryDto();
            deliveryDto.setId(delivery.getId());
            deliveryDto.setName(delivery.getName());
            deliveryDto.setPhoneNumber(delivery.getPhoneNumber());
            deliveryDto.setStreet(delivery.getStreet());
            deliveryDto.setBuilding(delivery.getBuilding());
            deliveryDto.setApartment(delivery.getApartment());
            deliveryDto.setDeliveryTime(delivery.getDeliveryTime());
            deliveryDto.setActualDeliveryTime(delivery.getActualDeliveryTime());
            deliveryDto.setChangeAmount(delivery.getChangeAmount());
            deliveryDto.setStatus(delivery.getStatus());
            ordersDto.setDelivery(deliveryDto);
        }
        if (orders.getOrderItems() != null) {
            List<OrderItemDto> orderItemsDto = new ArrayList<>();
            for (OrderItem orderItem : orders.getOrderItems()) {
                OrderItemDto orderItemDto = new OrderItemDto();
                orderItemDto.setId(orderItem.getId());
                orderItemDto.setQuantity(orderItem.getQuantity());
                orderItemDto.setProductName(orderItem.getProduct().getName());
                orderItemDto.setProductId(orderItem.getProduct().getId());
                orderItemsDto.add(orderItemDto);
            }
            ordersDto.setOrderItemsDto(orderItemsDto);
        }
        return ordersDto;
    }


    public Page<OrdersDto> toDto(Page<Orders> orders) {
        Page<OrdersDto> ordersDto = orders.map(this::toDto);
        return ordersDto;
    }
}
