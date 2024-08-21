package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface ProductService {



    void saveProduct(Product product);
    Optional<Product> getProduct(Long id);

    ProductDto getProductDto(Long id);

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

    List<Product> getAllProducts();

    List<ProductDto> getAllProductsDto();

    List<ProductDto> findByCategoryId(Long id);


    Map<String, List<Integer>> findTopProductsSalesByMonth(int quantityProduct, int months);
}
