package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    void saveProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
    void updateProduct(Long id, Product product);

    Product createProductFromDto(ProductDto productDto);

    void updateProductFromDto(Long id, ProductDto productDto);
    void deleteProduct(Product product);
    void deleteProduct(Long id);
    Page<Product> findAllProducts(int page, int pageSize);
    Page<Product> findProductsByRequest(int page, int pageSize, String search);

    long countProducts();
}
