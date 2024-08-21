package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.OrderItemAttributeDto;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemAttributeService {


    OrderItemAttribute saveOrderItemAttribute(OrderItemAttribute orderItemAttribute);

    OrderItemAttribute updateOrderItemAttribute(OrderItemAttribute orderItemAttribute);

    void deleteOrderItemAttribute(Long id);

    OrderItemAttribute getOrderItemAttribute(Long id);

    Iterable<OrderItemAttribute> getAllOrderItemAttributes();

    Iterable<OrderItemAttribute> getAllOrderItemAttributesByOrderItemId(Long orderItemId);

    void deleteAllOrderItemAttributesByOrderItemId(Long orderItemId);

    OrderItemAttribute saveOrderItemAttribute(OrderItemAttributeDto orderItemAttributeDto, OrderItem orderItem);
}
