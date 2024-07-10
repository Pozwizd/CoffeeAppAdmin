package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.Orders;
import com.spacelab.coffeeapp.repository.OrdersRepository;
import com.spacelab.coffeeapp.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImp implements OrderService {

    private final OrdersRepository orderRepository;
    @Override
    public void saveOrder(Orders order) {
        log.info("Save order: {}", order);
        orderRepository.save(order);
    }

    @Override
    public Orders getOrder(Long id) {
        log.info("Get order by id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void updateOrder(Long id, Orders order) {
        orderRepository.findById(id).map(order1 -> {
            order1.setDateTimeOfCreate(order.getDateTimeOfCreate());
            order1.setDateTimeOfUpdate(order.getDateTimeOfUpdate());
            order1.setDateTimeOfReady(order.getDateTimeOfReady());
            order1.setDelivery(order.getDelivery());
            order1.setOrderItems(order.getOrderItems());
            order1.setPayment(order.getPayment());
            order1.setCustomer(order.getCustomer());
            order1.setStatus(order.getStatus());
            orderRepository.save(order1);
            return order1;
        }).orElseThrow(() -> {
            throw new RuntimeException("Order not found");
        });
    }

    @Override
    public void deleteOrder(Orders order) {
        orderRepository.delete(order);
        log.info("Delete order: {}", order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Delete order by id: {}", id);

    }

    @Override
    public Page findAllOrders(int page, int pageSize) {
        return orderRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page findOrdersByRequest(int page, int pageSize, String search) {
        return null;
    }

    @Override
    public long countOrders() {
        return orderRepository.count();
    }
}
