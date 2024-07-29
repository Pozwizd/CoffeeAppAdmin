package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import com.spacelab.coffeeapp.repository.OrderItemAttributeRepository;
import com.spacelab.coffeeapp.service.OrderItemAttributeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemAttributeServiceImp implements OrderItemAttributeService {

    private final OrderItemAttributeRepository orderItemAttributeRepository;
    @Override
    public void saveOrderItemAttribute(OrderItemAttribute orderItemAttribute) {
        orderItemAttributeRepository.save(orderItemAttribute);
        log.info("OrderItemAttribute saved: {}", orderItemAttribute);
    }

    @Override
    public void updateOrderItemAttribute(OrderItemAttribute orderItemAttribute) {
        orderItemAttributeRepository.save(orderItemAttribute);
    }

    @Override
    public void deleteOrderItemAttribute(Long id) {
        orderItemAttributeRepository.deleteById(id);
    }

    @Override
    public OrderItemAttribute getOrderItemAttribute(Long id) {
        return orderItemAttributeRepository.findById(id).get();
    }

    @Override
    public Iterable<OrderItemAttribute> getAllOrderItemAttributes() {
        return orderItemAttributeRepository.findAll();
    }

    @Override
    public Iterable<OrderItemAttribute> getAllOrderItemAttributesByOrderItemId(Long orderItemId) {
        return orderItemAttributeRepository.findOrderItemAttributeByOrderItem_Id(orderItemId);
    }

    @Override
    public void deleteAllOrderItemAttributesByOrderItemId(Long orderItemId) {
        orderItemAttributeRepository.deleteByOrderItem_Id(orderItemId);
    }
}
