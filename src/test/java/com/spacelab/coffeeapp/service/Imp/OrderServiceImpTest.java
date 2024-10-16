package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrderItemDto;
import com.spacelab.coffeeapp.dto.OrdersDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Delivery;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.entity.OrderItem;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderServiceImpTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private OrderItemService orderItemService;

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
    void testUpdateOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderServiceImp.updateOrder(1L, order));
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
    void testGetPagedAllOrdersDto() {
        Page<Order> pagedOrders = new PageImpl<>(Collections.singletonList(order));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(pagedOrders);
        when(orderMapper.toDto(pagedOrders)).thenReturn(new PageImpl<>(Collections.singletonList(ordersDto)));

        Page<OrdersDto> result = orderServiceImp.getPagedAllOrdersDto(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
        verify(orderMapper, times(1)).toDto(pagedOrders);
    }

    @Test
    void testGetPagedAllOrdersDtoWithSearch() {
        Page<Order> pagedOrders = new PageImpl<>(Collections.singletonList(order));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(orderRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(pagedOrders);
        when(orderMapper.toDto(pagedOrders)).thenReturn(new PageImpl<>(Collections.singletonList(ordersDto)));

        Page<OrdersDto> result = orderServiceImp.getPagedAllOrdersDto(0, 10, "searchTerm");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
        verify(orderMapper, times(1)).toDto(pagedOrders);
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

    @Test
    void testCountAllOrders() {
        when(orderRepository.countAllOrders()).thenReturn(10);
        Integer count = orderServiceImp.countAllOrders();
        assertEquals(10, count);
        verify(orderRepository, times(1)).countAllOrders();
    }

    @Test
    void testCountTodayOrders() {
        when(orderRepository.countTodayOrders()).thenReturn(5);
        Integer count = orderServiceImp.countTodayOrders();
        assertEquals(5, count);
        verify(orderRepository, times(1)).countTodayOrders();
    }

    @Test
    void testCalculateTotalSales() {
        when(orderRepository.findAllDoneOrders()).thenReturn(500L);
        Long totalSales = orderServiceImp.calculateTotalSales();
        assertEquals(500L, totalSales);
        verify(orderRepository, times(1)).findAllDoneOrders();
    }

    @Test
    void testCalculateTodaySales() {
        when(orderRepository.findAllDoneOrdersToday()).thenReturn(100L);
        Long todaySales = orderServiceImp.calculateTodaySales();
        assertEquals(100L, todaySales);
        verify(orderRepository, times(1)).findAllDoneOrdersToday();
    }

    @Test
    void testCancelAllOrdersByCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);

        List<Order> orders = List.of(order);
        when(orderRepository.findByCustomerAndStatusNot(customer, Order.OrderStatus.DONE)).thenReturn(orders);
        when(orderRepository.saveAll(orders)).thenReturn(orders);

        List<Order> result = orderServiceImp.CancelAllOrdersByCustomer(customer);

        assertEquals(Order.OrderStatus.CANCELLED, result.get(0).getStatus());
        verify(orderRepository, times(1)).findByCustomerAndStatusNot(customer, Order.OrderStatus.DONE);
        verify(orderRepository, times(1)).saveAll(orders);
    }

    @Test
    void testGetLastOrdersForStatistics() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDto(anyList())).thenReturn(List.of(ordersDto));

        List<OrdersDto> result = orderServiceImp.getLastOrdersForStatistics();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toDto(anyList());
    }

    @Test
    void testSaveOrderFromDto_RemovesItemsNotInUpdatedOrder() {

        Order existingOrder = new Order();
        existingOrder.setId(1L);

        OrderItem existingItem1 = new OrderItem();
        existingItem1.setId(1L);

        OrderItem existingItem2 = new OrderItem();
        existingItem2.setId(2L);

        existingOrder.setOrderItems(Arrays.asList(existingItem1, existingItem2));

        OrderItemDto updatedItemDto = new OrderItemDto();
        updatedItemDto.setId(1L);

        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(1L);
        ordersDto.setOrderItemsDto(Collections.singletonList(updatedItemDto));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);
        when(orderItemService.saveOrderItem(any(OrderItemDto.class), any(Order.class)))
                .thenReturn(existingItem1);
        when(orderItemService.deleteAll(anyList())).thenReturn(true);

        Order result = orderServiceImp.saveOrderFromDto(ordersDto);

        assertEquals(1, result.getOrderItems().size());
        assertTrue(result.getOrderItems().contains(existingItem1));
        assertFalse(result.getOrderItems().contains(existingItem2));

        verify(orderItemService, times(1)).deleteAll(Collections.singletonList(existingItem2));
        verify(orderRepository, times(2)).save(any(Order.class));
    }


}