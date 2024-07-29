package com.spacelab.coffeeapp.service.Imp;


import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.repository.OrderItemRepository;
import com.spacelab.coffeeapp.service.OrderItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemServiceImp implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        log.info("Saving OrderItem: {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(OrderItem orderItem) {
        log.info("Deleting OrderItem: {}", orderItem);
        orderItemRepository.delete(orderItem);
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem getOrderItem(Long id) {
        return orderItemRepository.findById(id).get();
    }

    @Override
    public Page<OrderItem> findAllOrderItems(int page, int pageSize) {
        return orderItemRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public long countOrderItems() {
        return orderItemRepository.count();
    }

    @Override
    public void updateOrderItem(Long id, OrderItem orderItem) {
        orderItemRepository.findById(id).map(orderItem1 -> {
            orderItem.setProduct(orderItem1.getProduct());
            orderItem.setQuantity(orderItem1.getQuantity());
            return orderItemRepository.save(orderItem);
        }).orElseThrow(() -> {
            log.error("OrderItem not found");
            return new RuntimeException("OrderItem not found");
        });
    }

    @Override
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
        log.info("Deleted OrderItem with id: {}", id);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        log.info("Get order items by order id: {}", orderId);
        return orderItemRepository.findByOrderId(orderId);
    }
}
