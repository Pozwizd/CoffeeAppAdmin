package com.spacelab.coffeeapp.service.Imp;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.dto.TopProduct;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    void testFindProductsDtoByRequest_SearchIsEmpty() {
        Page<Product> productPage = new PageImpl<>(Collections.emptyList());
        Page<ProductDto> productDtoPage = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAll(any(Specification.class), any())).thenReturn(productPage);
        when(productMapper.toDtoListPage(productPage)).thenReturn(productDtoPage);

        Page<ProductDto> result = productService.findProductsDtoByRequest(1, 10, ""); // pageSize = 10, should not trigger error

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), any());
        verify(productMapper, times(1)).toDtoListPage(productPage);
    }


    @Test
    void testFindProductsDtoByRequest_SearchIsNotEmpty() {
        String search = "coffee";
        Page<Product> productPage = new PageImpl<>(Collections.emptyList());
        Page<ProductDto> productDtoPage = new PageImpl<>(Collections.emptyList());
        when(productRepository.findAll(any(Specification.class), any())).thenReturn(productPage);
        when(productMapper.toDtoListPage(productPage)).thenReturn(productDtoPage);

        Page<ProductDto> result = productService.findProductsDtoByRequest(1, 10, search);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), any());
        verify(productMapper, times(1)).toDtoListPage(productPage);
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

    @Test
    void testFindAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Product> result = productService.findAllProducts(0, 10);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testFindProductsByRequest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Product> result = productService.findProductsByRequest(0, 10, "search");

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testFindProductsDtoByRequest() {
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Specification.class), any())).thenReturn(page);
        when(productMapper.toDtoListPage(page)).thenReturn(new PageImpl<>(List.of(productDto)));

        Page<ProductDto> result = productService.findProductsDtoByRequest(0, 10, "search");

        assertEquals(1, result.getTotalElements());
        verify(productMapper, times(1)).toDtoListPage(page);
    }

    @Test
    void testGetTop3Products() {
        List<Object[]> topProducts = List.of(new Object[]{"Product1", 50L}, new Object[]{"Product2", 30L}, new Object[]{"Product3", 20L});
        when(productRepository.findTopProductsAndFrequency(any())).thenReturn(topProducts);

        List<TopProduct> result = productService.getTop3Products();

        assertEquals(3, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals(50.0, result.get(0).getPercentage());
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProductsDto() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDtoList(anyList())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.getAllProductsDto();

        assertEquals(1, result.size());
        verify(productMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testFindByCategoryId() {
        when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(product));
        when(productMapper.toDtoList(anyList())).thenReturn(List.of(productDto));

        List<ProductDto> result = productService.findByCategoryId(1L);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testFindTopProductsSalesByMonth() {
        int currentMonth = LocalDate.now().getMonthValue();
        int previousMonth = LocalDate.now().minusMonths(1).getMonthValue();

        List<Object[]> sales = List.of(
                new Object[]{"Product1", currentMonth, 100},
                new Object[]{"Product2", previousMonth, 50}
        );

        when(productRepository.findTopProductsSalesByMonth(any(), any())).thenReturn(sales);

        Map<String, List<Integer>> result = productService.findTopProductsSalesByMonth(2, 2);
        System.out.println("Product Sales By Month: " + result);

        // Проверяем результаты
        assertEquals(2, result.size());
        assertEquals(50, result.get("Product2").get(1));
    }

    @Test
    void testUpdateProductFromDto_ProductNotFound() {
        // Мокаем, что продукт не найден по id
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProductFromDto(1L, productDto);
        });

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, product);
        });

        assertEquals("Product not found", exception.getMessage());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }


}
