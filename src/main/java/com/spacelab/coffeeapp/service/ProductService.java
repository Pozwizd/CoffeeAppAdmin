package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {

    @Transactional
    Map<String, int[]> getTopProductsSalesLastMonths(int months);

    void saveProduct(Product product);
    Product getProduct(Long id);

    ProductDto getProductDto(Long id);

    List<Product> getAllProducts();

    List<ProductDto> getAllProductDto();

    Product createProduct(Product product);

    Product createProductFromDto(ProductDto productDto);

    void updateProduct(Long id, Product product);

    void updateProductFromDto(Long id, ProductDto productDto);

    void deleteProduct(Product product);

    void deleteProduct(Long id);

    Page<Product> findAllProducts(int page, int pageSize);

    Page<Product> findProductsByRequest(int page, int pageSize, String search);

    Page<ProductDto> findProductsDtoByRequest(int page, int pageSize, String search);

    long countProducts();

    List<Product> getProductsByCategory(Long categoryId);

    List<ProductDto> getProductsDtoByCategory(Long categoryId);


    List<TopProduct> getTop3Products();

}
