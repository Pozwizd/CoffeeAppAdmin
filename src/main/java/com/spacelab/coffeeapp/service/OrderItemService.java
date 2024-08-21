package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.OrderItemDto;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemService {

    OrderItem saveOrderItem(OrderItem orderItem);

    void deleteOrderItem(OrderItem orderItem);

    List<OrderItem> getAllOrderItems();

    OrderItem getOrderItem(Long id);

    Page<OrderItem> findAllOrderItems(int page, int pageSize);

    long countOrderItems();

    OrderItem updateOrderItem(OrderItem orderItem);

    void deleteOrderItem(Long id);

    List<OrderItem> getOrderItemsByOrderId(Long orderId);


    OrderItem saveOrderItem(OrderItemDto orderItemDto, Order savedOrder);

    boolean deleteAll(List<OrderItem> itemsToRemove);
}
