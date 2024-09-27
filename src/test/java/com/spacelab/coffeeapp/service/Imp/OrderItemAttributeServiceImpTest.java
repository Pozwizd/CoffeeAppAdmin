package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.OrderItemAttributeDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import com.spacelab.coffeeapp.repository.OrderItemAttributeRepository;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.AttributeValueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderItemAttributeServiceImpTest {

    @Mock
    private OrderItemAttributeRepository orderItemAttributeRepository;

    @Mock
    private AttributeProductService attributeProductService;

    @Mock
    private AttributeValueService attributeValueService;

    @InjectMocks
    private OrderItemAttributeServiceImp orderItemAttributeService;

    private OrderItemAttribute orderItemAttribute;
    private OrderItem orderItem;
    private AttributeProduct attributeProduct;
    private AttributeValue attributeValue;
    private OrderItemAttributeDto orderItemAttributeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        attributeProduct = new AttributeProduct();
        attributeProduct.setId(1L);

        attributeValue = new AttributeValue();
        attributeValue.setId(1L);
        attributeValue.setPrice(100.0);
        attributeValue.setPriceWithDiscount(90.0);

        orderItem = new OrderItem();
        orderItem.setId(1L);

        orderItemAttribute = new OrderItemAttribute();
        orderItemAttribute.setId(1L);
        orderItemAttribute.setOrderItem(orderItem);
        orderItemAttribute.setAttributeProduct(attributeProduct);
        orderItemAttribute.setAttributeValue(attributeValue);

        orderItemAttributeDto = new OrderItemAttributeDto();
        orderItemAttributeDto.setProductAttributeId(1L);
        orderItemAttributeDto.setAttributeValueId(1L);
    }

    @Test
    void testDeleteOrderItemAttribute() {
        orderItemAttributeService.deleteOrderItemAttribute(1L);
        verify(orderItemAttributeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetOrderItemAttribute() {
        when(orderItemAttributeRepository.findById(1L)).thenReturn(Optional.of(orderItemAttribute));

        OrderItemAttribute result = orderItemAttributeService.getOrderItemAttribute(1L);

        assertEquals(orderItemAttribute, result);
        verify(orderItemAttributeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllOrderItemAttributes() {
        orderItemAttributeService.getAllOrderItemAttributes();
        verify(orderItemAttributeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrderItemAttributesByOrderItemId() {
        orderItemAttributeService.getAllOrderItemAttributesByOrderItemId(1L);
        verify(orderItemAttributeRepository, times(1)).findOrderItemAttributeByOrderItem_Id(1L);
    }

    @Test
    void testDeleteAllOrderItemAttributesByOrderItemId() {
        orderItemAttributeService.deleteAllOrderItemAttributesByOrderItemId(1L);
        verify(orderItemAttributeRepository, times(1)).deleteByOrderItem_Id(1L);
    }

    @Test
    void testSaveOrderItemAttribute() {
        when(orderItemAttributeRepository.save(any(OrderItemAttribute.class))).thenReturn(orderItemAttribute);

        OrderItemAttribute savedAttribute = orderItemAttributeService.saveOrderItemAttribute(orderItemAttribute);

        assertEquals(orderItemAttribute, savedAttribute);
        verify(orderItemAttributeRepository, times(1)).save(orderItemAttribute);
    }

    @Test
    void testUpdateOrderItemAttribute() {
        when(orderItemAttributeRepository.save(any(OrderItemAttribute.class))).thenReturn(orderItemAttribute);

        OrderItemAttribute updatedAttribute = orderItemAttributeService.updateOrderItemAttribute(orderItemAttribute);

        assertEquals(orderItemAttribute, updatedAttribute);
        verify(orderItemAttributeRepository, times(1)).save(orderItemAttribute);
    }

    @Test
    void testSaveOrderItemAttributeDto_New() {
        when(attributeProductService.getAttributeProduct(1L)).thenReturn(Optional.of(attributeProduct));
        when(attributeValueService.getAttributeValue(1L)).thenReturn(Optional.of(attributeValue));
        when(orderItemAttributeRepository.save(any(OrderItemAttribute.class))).thenReturn(orderItemAttribute);

        OrderItemAttribute savedAttribute = orderItemAttributeService.saveOrderItemAttribute(orderItemAttributeDto, orderItem);

        assertNotNull(savedAttribute);
        verify(orderItemAttributeRepository, times(1)).save(any(OrderItemAttribute.class));
    }

    @Test
    void testSaveOrderItemAttributeDto_Update() {
        when(orderItemAttributeRepository.findById(1L)).thenReturn(Optional.of(orderItemAttribute));
        when(attributeProductService.getAttributeProduct(1L)).thenReturn(Optional.of(attributeProduct));
        when(attributeValueService.getAttributeValue(1L)).thenReturn(Optional.of(attributeValue));
        when(orderItemAttributeRepository.save(any(OrderItemAttribute.class))).thenReturn(orderItemAttribute);

        orderItemAttributeDto.setId(1L);
        OrderItemAttribute updatedAttribute = orderItemAttributeService.saveOrderItemAttribute(orderItemAttributeDto, orderItem);

        assertNotNull(updatedAttribute);
        verify(orderItemAttributeRepository, times(1)).save(any(OrderItemAttribute.class));
    }
}
