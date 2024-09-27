package com.spacelab.coffeeapp.service.Imp;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.Category;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.ProductMapper;
import com.spacelab.coffeeapp.repository.ProductRepository;
import com.spacelab.coffeeapp.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImp productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Создаем тестовый продукт
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setStatus(Product.Status.ACTIVE);

        // Создаем тестовый ProductDto
        productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setStatus("ACTIVE");
        productDto.setCategory("1");
    }

    @Test
    void testSaveProduct() {
        productService.saveProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> foundProduct = productService.getProduct(1L);
        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
    }

    @Test
    void testGetProductDto() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto foundProductDto = productService.getProductDto(1L);
        assertEquals("Test Product", foundProductDto.getName());
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);
        assertEquals("Test Product", createdProduct.getName());
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setStatus(Product.Status.ACTIVE);

        productService.updateProduct(1L, updatedProduct);

        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals("Updated Product", product.getName());
    }

    @Test
    void testDeleteProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).save(product);
        assertTrue(product.getDeleted()); // Предполагается, что у вас есть метод setDeleted в Product
    }

    @Test
    void testCreateProductFromDto() {
        when(categoryService.getCategory(1L)).thenReturn(Optional.of(new Category()));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProductFromDto(productDto);
        assertEquals("Test Product", createdProduct.getName());
    }

    @Test
    void testUpdateProductFromDto() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryService.getCategory(1L)).thenReturn(Optional.of(new Category()));

        productService.updateProductFromDto(1L, productDto);

        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals("Test Product", product.getName());
    }

    @Test
    void testCountProducts() {
        when(productRepository.count()).thenReturn(10L);
        long count = productService.countProducts();
        assertEquals(10L, count);
    }

}
