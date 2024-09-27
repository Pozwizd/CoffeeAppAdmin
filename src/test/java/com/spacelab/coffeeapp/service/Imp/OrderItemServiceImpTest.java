package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrderItemDto;
import com.spacelab.coffeeapp.entity.Order;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.repository.OrderItemRepository;
import com.spacelab.coffeeapp.service.OrderItemAttributeService;
import com.spacelab.coffeeapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderItemServiceImpTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private OrderItemAttributeService orderItemAttributeService;

    @InjectMocks
    private OrderItemServiceImp orderItemService;

    private OrderItem orderItem;
    private Order savedOrder;
    private OrderItemDto orderItemDto;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);

        savedOrder = new Order();
        savedOrder.setId(1L);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(savedOrder);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(2);
        orderItemDto.setAttributes(new ArrayList<>());
    }

    @Test
    void testSaveOrderItem() {
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem savedOrderItem = orderItemService.saveOrderItem(orderItem);

        assertNotNull(savedOrderItem);
        assertEquals(orderItem, savedOrderItem);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).delete(orderItem);

        orderItemService.deleteOrderItem(orderItem);

        verify(orderItemRepository, times(1)).delete(orderItem);
    }

    @Test
    void testGetAllOrderItems() {
        List<OrderItem> orderItems = Arrays.asList(orderItem);
        when(orderItemRepository.findAll()).thenReturn(orderItems);

        List<OrderItem> result = orderItemService.getAllOrderItems();

        assertEquals(orderItems, result);
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderItem() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));

        OrderItem result = orderItemService.getOrderItem(1L);

        assertEquals(orderItem, result);
        verify(orderItemRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllOrderItems() {
        List<OrderItem> orderItems = Arrays.asList(orderItem);
        Page<OrderItem> page = new PageImpl<>(orderItems);
        when(orderItemRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<OrderItem> result = orderItemService.findAllOrderItems(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(orderItemRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testCountOrderItems() {
        when(orderItemRepository.count()).thenReturn(5L);

        long count = orderItemService.countOrderItems();

        assertEquals(5L, count);
        verify(orderItemRepository, times(1)).count();
    }

    @Test
    void testUpdateOrderItem() {
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem updatedOrderItem = orderItemService.updateOrderItem(orderItem);

        assertNotNull(updatedOrderItem);
        assertEquals(orderItem, updatedOrderItem);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void testDeleteOrderItemById() {
        doNothing().when(orderItemRepository).deleteById(1L);

        orderItemService.deleteOrderItem(1L);

        verify(orderItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetOrderItemsByOrderId() {
        List<OrderItem> orderItems = Arrays.asList(orderItem);
        when(orderItemRepository.findByOrderId(1L)).thenReturn(orderItems);

        List<OrderItem> result = orderItemService.getOrderItemsByOrderId(1L);

        assertEquals(orderItems, result);
        verify(orderItemRepository, times(1)).findByOrderId(1L);
    }

    @Test
    void testSaveOrderItemDto_New() {
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem result = orderItemService.saveOrderItem(orderItemDto, savedOrder);

        assertNotNull(result);
        assertEquals(orderItem, result);
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
    }

    @Test
    void testSaveOrderItemDto_Update() {
        orderItemDto.setId(1L);
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem));
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem result = orderItemService.saveOrderItem(orderItemDto, savedOrder);

        assertNotNull(result);
        assertEquals(orderItem, result);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testDeleteAll() {
        List<OrderItem> itemsToRemove = Collections.singletonList(orderItem);

        boolean result = orderItemService.deleteAll(itemsToRemove);

        assertTrue(result);
        verify(orderItemRepository, times(1)).delete(orderItem);
    }

    @Test
    void testCreateNewOrderItem() {
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem result = orderItemService.saveOrderItem(orderItemDto, savedOrder);

        assertNotNull(result);
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
    }

}
