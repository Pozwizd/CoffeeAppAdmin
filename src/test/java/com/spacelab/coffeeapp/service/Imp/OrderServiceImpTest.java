package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.mapper.OrderMapper;
import com.spacelab.coffeeapp.repository.OrderRepository;
import com.spacelab.coffeeapp.service.DeliveryService;
import com.spacelab.coffeeapp.service.OrderItemService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderServiceImpTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImp orderServiceImp;

    private Order order;
    private OrdersDto ordersDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        order = new Order();
        order.setId(1L);
        order.setStatus(Order.OrderStatus.NEW);
        Customer customer = new Customer();
        customer.setId(1L);
        order.setCustomer(customer);
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        order.setDelivery(delivery);


        ordersDto = new OrdersDto();
        ordersDto.setStatus(Order.OrderStatus.valueOf("NEW"));

        ordersDto.setId(1L);

    }

    @Test
    void testSaveOrder() {
        orderServiceImp.saveOrder(order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order foundOrder = orderServiceImp.getOrder(1L);
        assertNotNull(foundOrder);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderServiceImp.getOrder(1L));
    }

    @Test
    void testGetOrderDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(ordersDto);

        OrdersDto result = orderServiceImp.getOrderDto(1L);
        assertNotNull(result);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderServiceImp.getAllOrders();
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderServiceImp.updateOrder(1L, order);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrder() {
        orderServiceImp.deleteOrder(order);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void testDeleteOrderById() {
        orderServiceImp.deleteOrder(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAllOrdersWithSearch() {
        Page<Order> pagedOrders = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAll(any(Specification.class), any())).thenReturn(pagedOrders);

        Page<Order> result = orderServiceImp.findAllOrders(0, 10, "customerName");
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void testSaveOrderFromDto_NewOrder() {

        ordersDto.setId(null);
        ordersDto.setOrderItemsDto(new ArrayList<>());

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(ordersDto);

        Order result = orderServiceImp.saveOrderFromDto(ordersDto);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }



    @Test
    void testSaveOrderFromDto_UpdateOrder() {
        ordersDto.setId(1L);
        ordersDto.setOrderItemsDto(new ArrayList<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderServiceImp.saveOrderFromDto(ordersDto);

        assertNotNull(result);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

}
