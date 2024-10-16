package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.ProductMapper;
import com.spacelab.coffeeapp.repository.ProductRepository;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.spacelab.coffeeapp.specification.ProductSpecification.*;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
        log.info("Product saved successfully");
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id);
    }

    @Override
    public ProductDto getProductDto(Long id) {
        return productMapper.toDto(getProduct(id).get());
    }

    @Override
    public Product createProduct(Product product) {
        log.info("Creating product");
        saveProduct(product);
        return product;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        log.info("Updating product with id: {}", id);
        productRepository.findById(id).map(product1 -> {
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setStatus(product.getStatus());
            product1.setCategory(product.getCategory());
            productRepository.save(product1);
            return product1;
        }).orElseThrow(() -> {
            log.error("Product not found");
            return new RuntimeException("Product not found");
        });
    }

    @Override
    public Product createProductFromDto(ProductDto productDto) {
        log.info("Creating product from dto");

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStatus(Product.Status.valueOf(productDto.getStatus()));
        product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory())).get());
        saveProduct(product);
        return product;
    }

    @Override
    public void updateProductFromDto(Long id, ProductDto productDto) {
        log.info("Updating product from dto with id: {}", id);
        productRepository.findById(id).map(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setStatus(Product.Status.valueOf(productDto.getStatus()));
            product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory())).get());
            productRepository.save(product);
            return product;
        }).orElseThrow(() -> {
            log.error("Product not found");
            return new RuntimeException("Product not found");
        });
    }



    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).map(product -> {
            product.setDeleted(true);
            productRepository.save(product);
            return product;
        });
        log.info("Product deleted successfully by id: {}", id);
    }

    @Override
    public Page<Product> findAllProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get products with pageable: {}", pageable);
        return productRepository.findAll(byNotDeleted(),pageable);
    }

    @Override
    public Page<Product> findProductsByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get products with pageable and specification: {}", search);
        return productRepository.findAll(search(search).and(byNotDeleted()), pageable);
    }

    @Override
    public Page<ProductDto> findProductsDtoByRequest(int page, int pageSize, String search) {
        if (search.isEmpty()) {
            return productMapper.toDtoListPage(findAllProducts(page, pageSize));
        } else {
            return productMapper.toDtoListPage(findProductsByRequest(page, pageSize, search));
        }
    }


    @Override
    public long countProducts() {
        return productRepository.count();
    }


    @Override
    public List<TopProduct> getTop3Products() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> results = productRepository.findTopProductsAndFrequency(pageable);

        double totalQuantity = results.stream()
                .mapToDouble(result -> ((Number) result[1]).doubleValue())
                .sum();

        return results.stream()
                .map(result -> new TopProduct(
                        (String) result[0],
                        (((Number) result[1]).doubleValue() / totalQuantity) * 100
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProducts() {
        log.info("Get all products");
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> getAllProductsDto() {
        return productMapper.toDtoList(getAllProducts());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return productMapper.toDtoList(productRepository.findAll(byCategoryId(id)));
    }

    @Override
    public Map<String, List<Integer>> findTopProductsSalesByMonth(int quantityProduct, int months) {
        LocalDateTime startDate = LocalDate.now().minusMonths(months).atStartOfDay();
        Pageable pageable = PageRequest.of(0, quantityProduct);  // Ограничиваем количество продуктов
        List<Object[]> results = productRepository.findTopProductsSalesByMonth(startDate, pageable);

        Map<String, List<Integer>> productSalesByMonth = new HashMap<>();

        for (Object[] result : results) {
            String productName = (String) result[0];
            int month = ((Number) result[1]).intValue();
            int purchaseCount = ((Number) result[2]).intValue();

            productSalesByMonth.putIfAbsent(productName, new ArrayList<>(Collections.nCopies(months, 0)));

            int index = (int) ChronoUnit.MONTHS.between(
                    startDate.toLocalDate().withDayOfMonth(1),
                    LocalDate.of(LocalDate.now().getYear(), month, 1)
            );

            if (index >= 0 && index < months) {
                productSalesByMonth.get(productName).set(index, purchaseCount);
            }
        }
        return productSalesByMonth;
    }
}
