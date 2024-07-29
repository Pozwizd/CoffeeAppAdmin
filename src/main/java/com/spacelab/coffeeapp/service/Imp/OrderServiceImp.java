package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.mapper.OrderMapper;
import com.spacelab.coffeeapp.repository.OrderRepository;
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

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    @Override
    public void saveOrder(Order order) {
        log.info("Save order: {}", order);
        orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        log.info("Get order by id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public OrdersDto getOrderDto(Long id) {
        log.info("Get orderDto by id: {}", id);
        return orderMapper.toDto(getOrder(id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void updateOrder(Long id, Order order) {
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
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
        log.info("Delete order: {}", order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Delete order by id: {}", id);

    }

    @Override
    public Page<Order> findAllOrders(int page, int pageSize) {
        return orderRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Page<OrdersDto> getPagedAllOrdersDto(int page, int pageSize) {
        return orderMapper.toDto(findAllOrders(page, pageSize));
    }


    // Добавить поиск по номеру телефона
    @Override
    public Page<OrdersDto> findOrdersByRequest(int page, int pageSize, String search) {
        return null;
    }

    @Override
    public Integer countAllOrders() {
        return orderRepository.countAllOrders();
    }

    @Override
    public Integer countTodayOrders() {
        return orderRepository.countTodayOrders();
    }

    @Override
    public Long calculateTotalSales() {
        return orderRepository.findAllDoneOrders();
    }

    @Override
    public Long calculateTodaySales() {
        return orderRepository.findAllDoneOrdersToday();
    }


}
