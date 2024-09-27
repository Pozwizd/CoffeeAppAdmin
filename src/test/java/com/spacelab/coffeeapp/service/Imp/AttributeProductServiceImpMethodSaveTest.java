package com.spacelab.coffeeapp.service.Imp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.AttributeProductMapper;
import com.spacelab.coffeeapp.repository.AttributeProductRepository;
import com.spacelab.coffeeapp.service.AttributeValueService;
import com.spacelab.coffeeapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttributeProductServiceImpMethodSaveTest {

    @Mock
    private AttributeProductRepository attributeProductRepository;

    @Mock
    private AttributeValueService attributeValueService;

    @Mock
    private ProductService productService;


    @InjectMocks
    private AttributeProductServiceImp attributeProductService;

    private AttributeProductDto attributeProductDto;
    private AttributeProduct attributeProduct;
    private AttributeProduct attributeProduct2;
    private Product product;
    private AttributeValue attributeValue;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        attributeProductDto = new AttributeProductDto();
        attributeProductDto.setId(1L);
        attributeProductDto.setName("Test Attribute Product");
        attributeProductDto.setType("Basic");
        attributeProductDto.setStatus(true);
        attributeProductDto.setProductId(List.of(1L));

        attributeProduct = new AttributeProduct();
        attributeProduct.setId(1L);
        attributeProduct.setName("Test Attribute Product");

        attributeProduct2 = new AttributeProduct();
        attributeProduct.setId(2L);
        attributeProduct.setName("Test Attribute Product");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        attributeValue = new AttributeValue();
        attributeValue.setId(1L);
        attributeValue.setName("Test Attribute Value");

        attributeProductDto.setAttributeValues(List.of(new AttributeValueDto(1L, "Test", "Description", 100.0, 90.0),
                new AttributeValueDto(null, "Test", "Description", 100.0, 90.0)));
    }

    @Test
    public void testSaveAttributeProductFromDto_CreateNew() {

        attributeProductDto.setId(null);
        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(productService.getProduct(anyLong())).thenReturn(Optional.of(product));
        when(attributeProductRepository.save(any(AttributeProduct.class))).thenReturn(attributeProduct);
        when(attributeValueService.getAttributeValue(anyLong())).thenReturn(Optional.of(attributeValue));
        when(attributeValueService.saveAttributeValue(any(AttributeValue.class))).thenReturn(attributeValue);

        Boolean result = attributeProductService.saveAttributeProductFromDto(attributeProductDto);

        assertTrue(result);
        verify(attributeProductRepository, times(2)).save(any(AttributeProduct.class));
        verify(productService, times(1)).getProduct(1L);
        verify(attributeValueService, times(1)).getAttributeValue(1L);
        verify(attributeValueService, times(2)).saveAttributeValue(any(AttributeValue.class));
    }

    @Test
    public void testSaveAttributeProductFromDto_UpdateExisting() {
        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.of(attributeProduct));
        when(productService.getProduct(anyLong())).thenReturn(Optional.of(product));
        when(attributeProductRepository.save(any(AttributeProduct.class))).thenReturn(attributeProduct);
        when(attributeValueService.getAttributeValue(anyLong())).thenReturn(Optional.of(attributeValue));
        when(attributeValueService.saveAttributeValue(any(AttributeValue.class))).thenReturn(attributeValue);

        Boolean result = attributeProductService.saveAttributeProductFromDto(attributeProductDto);

        assertTrue(result);
        verify(attributeProductRepository, times(2)).save(any(AttributeProduct.class));
        verify(productService, times(1)).getProduct(1L);
        verify(attributeValueService, times(1)).getAttributeValue(1L);
        verify(attributeValueService, times(2)).saveAttributeValue(any(AttributeValue.class));
    }

    @Test
    public void testSaveAttributeProductFromDto_RemoveOldProducts() {
        Product oldProduct = new Product();
        oldProduct.setId(2L);
        attributeProduct.setProducts(List.of(oldProduct));

        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.of(attributeProduct));
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));
        when(attributeProductRepository.save(any(AttributeProduct.class))).thenReturn(attributeProduct);

        Boolean result = attributeProductService.saveAttributeProductFromDto(attributeProductDto);

        assertTrue(result);
        verify(attributeProductRepository, times(2)).save(any(AttributeProduct.class));
        verify(productService, times(1)).getProduct(1L);
        verify(productService, never()).getProduct(2L);
    }

    @Test
    public void testSaveAttributeProductFromDto2_RemoveOldAttributeValues() {
        AttributeValue oldAttributeValue = new AttributeValue();
        oldAttributeValue.setId(2L);
        attributeProduct.setAttributeValues(List.of(oldAttributeValue));

        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.of(attributeProduct));
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));
        when(attributeProductRepository.save(any(AttributeProduct.class))).thenReturn(attributeProduct);
        when(attributeValueService.getAttributeValue(1L)).thenReturn(Optional.of(attributeValue));

        Boolean result = attributeProductService.saveAttributeProductFromDto(attributeProductDto);

        assertTrue(result);
        verify(attributeValueService, times(1)).deleteAttributeValueById(2L);
        verify(attributeValueService, times(2)).saveAttributeValue(any(AttributeValue.class));
    }

    @Test
    public void testSaveAttributeProductFromDto_EmptyAttributeValues() {
        attributeProductDto.setAttributeValues(new ArrayList<>());
        AttributeValue oldAttributeValue = new AttributeValue();
        oldAttributeValue.setId(2L);
        attributeProduct.setAttributeValues(List.of(oldAttributeValue));

        when(attributeProductRepository.findById(anyLong())).thenReturn(Optional.of(attributeProduct));
        when(productService.getProduct(1L)).thenReturn(Optional.of(product));
        when(attributeProductRepository.save(any(AttributeProduct.class))).thenReturn(attributeProduct);
        when(attributeValueService.getAttributeValue(1L)).thenReturn(Optional.of(attributeValue));

        Boolean result = attributeProductService.saveAttributeProductFromDto(attributeProductDto);

        assertTrue(result);
    }

}
