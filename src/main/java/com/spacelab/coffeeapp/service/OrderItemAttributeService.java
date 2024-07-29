package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemAttributeService {


    void saveOrderItemAttribute(OrderItemAttribute orderItemAttribute);

    void updateOrderItemAttribute(OrderItemAttribute orderItemAttribute);

    void deleteOrderItemAttribute(Long id);

    OrderItemAttribute getOrderItemAttribute(Long id);

    Iterable<OrderItemAttribute> getAllOrderItemAttributes();

    Iterable<OrderItemAttribute> getAllOrderItemAttributesByOrderItemId(Long orderItemId);

    void deleteAllOrderItemAttributesByOrderItemId(Long orderItemId);
}
