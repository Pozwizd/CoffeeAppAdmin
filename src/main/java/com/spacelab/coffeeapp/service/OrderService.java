package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    public void saveOrder(Orders order);

    public Orders getOrder(Long id);

    public List<Orders> getAllOrders();

    public void updateOrder(Long id, Orders order);

    public void deleteOrder(Orders order);

    public void deleteOrder(Long id);

    public Page findAllOrders(int page, int pageSize);

    public Page findOrdersByRequest(int page, int pageSize, String search);

    public long countOrders();

}
