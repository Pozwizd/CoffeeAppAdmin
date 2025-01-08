package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.ProductMapper;
import com.spacelab.coffeeapp.repository.ProductRepository;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
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
@Slf4j
@Transactional
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    public ProductServiceImp(ProductRepository productRepository,
                             ProductMapper productMapper,
                             @Lazy CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
        log.info("Product saved successfully: {}", product);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        log.info("Fetching product with ID: {}", id);
        return productRepository.findById(id);
    }

    @Override
    public ProductDto getProductDto(Long id) {
        return getProduct(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    @Override
    public Product createProduct(Product product) {
        log.info("Creating product: {}", product);
        saveProduct(product);
        return product;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        log.info("Updating product with ID: {}", id);
        productRepository.findById(id).ifPresentOrElse(existingProduct -> {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setStatus(product.getStatus());
            existingProduct.setCategory(product.getCategory());
            productRepository.save(existingProduct);
            log.info("Product updated: {}", existingProduct);
        }, () -> {
            throw new RuntimeException("Product not found with ID: " + id);
        });
    }

    @Override
    public Product createProductFromDto(ProductDto productDto) {
        log.info("Creating product from DTO: {}", productDto);
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStatus(Product.Status.valueOf(productDto.getStatus()));
        product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory()))
                .orElseThrow(() -> new RuntimeException("Category not found for ID: " + productDto.getCategory())));
        saveProduct(product);
        return product;
    }

    @Override
    public void updateProductFromDto(Long id, ProductDto productDto) {
        log.info("Updating product from DTO with ID: {}", id);
        try {
            productRepository.findById(id).ifPresentOrElse(product -> {
                product.setName(productDto.getName());
                product.setDescription(productDto.getDescription());
                product.setStatus(Product.Status.valueOf(productDto.getStatus()));
                product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory()))
                        .orElseThrow(() -> new RuntimeException("Category not found for ID: " + productDto.getCategory())));
                productRepository.save(product);
                log.info("Product updated from DTO: {}", productDto);
            }, () -> {
                throw new RuntimeException("Product not found with ID: " + id);
            });
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(product -> {
            product.setDeleted(true);
            productRepository.save(product);
            log.info("Soft-deleted product with ID: {}", id);
        }, () -> {
            throw new RuntimeException("Product not found with ID: " + id);
        });
    }

    @Override
    public Page<Product> findAllProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Fetching products with pageable: {}", pageable);
        return productRepository.findAll(byNotDeleted(), pageable);
    }

    @Override
    public Page<Product> findProductsByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Fetching products with search term: {}", search);
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
        long count = productRepository.count();
        log.info("Total number of products: {}", count);
        return count;
    }

    @Override
    public List<TopProduct> getTop3Products() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> results = productRepository.findTopProductsAndFrequency(pageable);

        double totalQuantity = results.stream()
                .mapToDouble(result -> ((Number) result[1]).doubleValue())
                .sum();

        if (totalQuantity == 0) {
            log.warn("No products found for calculating top products");
            return Collections.emptyList();
        }

        return results.stream()
                .map(result -> new TopProduct(
                        (String) result[0],
                        (((Number) result[1]).doubleValue() / totalQuantity) * 100
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll(byNotDeleted());
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
        LocalDate startDate = LocalDate.now().minusMonths(months).withDayOfMonth(1);
        Pageable pageable = PageRequest.of(0, quantityProduct);
        List<Object[]> results = productRepository.findTopProductsSalesByMonth(startDate.atStartOfDay(), pageable);

        Map<String, List<Integer>> productSalesByMonth = new LinkedHashMap<>();

        for (Object[] result : results) {
            String productName = (String) result[0];
            int month = ((Number) result[1]).intValue();
            int purchaseCount = ((Number) result[2]).intValue();

            productSalesByMonth.putIfAbsent(productName, new ArrayList<>(Collections.nCopies(months, 0)));

            int index = months - (int) ChronoUnit.MONTHS.between(
                    startDate,
                    LocalDate.of(LocalDate.now().getYear(), month, 1)
            ) - 1;

            if (index >= 0 && index < months) {
                productSalesByMonth.get(productName).set(index, purchaseCount);
            }
        }
        return productSalesByMonth;
    }

//    @Override
//    public List<Product> findProductsByIds(List<Long> newProductIds) {
//        return productRepository.findAllById(newProductIds);
//    }
}
