package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    public void saveOrder(Order order);

    public Order getOrder(Long id);

    public OrdersDto getOrderDto(Long id);

    public List<Order> getAllOrders();

    public void updateOrder(Long id, Order order);

    public void updateOrderFromDto(Long id, OrdersDto order);

    public void deleteOrder(Order order);

    public void deleteOrder(Long id);

    Page<Order> findAllOrders(int page, int pageSize, String search);


    Page<OrdersDto> getPagedAllOrdersDto(int page, int pageSize);

    Page<OrdersDto> getPagedAllOrdersDto(int page, int pageSize, String search);

    Integer countAllOrders();

    Integer countTodayOrders();

    Long calculateTotalSales();

    Long calculateTodaySales();

    Order saveOrderFromDto(OrdersDto ordersDto);

    List<OrdersDto> getLastOrdersForStatistics();
}
