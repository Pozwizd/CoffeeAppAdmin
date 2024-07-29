package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.ProductMapper;
import com.spacelab.coffeeapp.repository.OrderItemRepository;
import com.spacelab.coffeeapp.repository.ProductRepository;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.AttributeValueService;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import com.spacelab.coffeeapp.specification.ProductSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService {

    private final CategoryService categoryService;
    private final AttributeProductService attributeProductService;
    private final AttributeValueService attributeValueService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Map<String, int[]> getTopProductsSalesLastMonths(int months) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(months);

        List<Object[]> topProducts = orderItemRepository.findTop4Products(startDate, endDate);
        Map<String, int[]> productSalesMap = new HashMap<>();

        for (Object[] product : topProducts) {
            Long productId = (Long) product[0];
            String productName = "Product Name";
            List<Object[]> salesData = orderItemRepository.countProductSalesByMonth(productId, startDate, endDate);

            int[] salesCounts = new int[months]; // массив для хранения продаж по месяцам
            for (Object[] sales : salesData) {
                int year = (int) sales[0];
                int month = (int) sales[1];
                int count = ((Number) sales[2]).intValue();

                // Рассчитать индекс для массива
                YearMonth currentMonth = YearMonth.now();
                YearMonth salesMonth = YearMonth.of(year, month);

                // Рассчитать количество месяцев между текущим месяцем и месяцем продажи
                long monthsBetween = ChronoUnit.MONTHS.between(salesMonth, currentMonth);
                // Индекс для массива будет обратным
                int index = (int) (months - monthsBetween - 1);

                // Заполнить массив
                if (index >= 0 && index < months) {
                    salesCounts[index] = count;
                }
            }

            productSalesMap.put(productName, salesCounts);
        }

        return productSalesMap;
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
        log.info("Product saved successfully");
    }

    @Override
    public Product getProduct(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id).get();
    }

    @Override
    public ProductDto getProductDto(Long id) {
        return productMapper.toDto(getProduct(id));
    }

    @Override
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> getAllProductDto() {
        return List.of();
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        log.info("Updating product with id: {}", id);
        productRepository.findById(id).map(product1 -> {
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
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
        product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory())));

        saveProduct(product);
        product.setId(productRepository.findMaxId());

        if (productDto.getAttributeProducts() != null) {
            List<AttributeProduct> attributeProducts = productDto.getAttributeProducts().stream().map(attributeProductDto -> {
                AttributeProduct attributeProduct = new AttributeProduct();
                attributeProduct.setName(attributeProductDto.getName());
                attributeProduct.setType(AttributeProduct.TypeAttribute.valueOf(attributeProductDto.getType()));
                attributeProduct.setProduct(product); // связываем с продуктом
                attributeProductService.saveAttributeProduct(attributeProduct);
                attributeProduct.setId(attributeProductService.findMaxId());

                if (attributeProductDto.getAttributeValues() == null) {
                    List<AttributeValue> attributeValues = attributeProductDto.getAttributeValues().stream().map(attributeValueDto -> {
                        AttributeValue attributeValue = new AttributeValue();
                        attributeValue.setName(attributeValueDto.getName());
                        attributeValue.setPrice(attributeValueDto.getPrice());
                        attributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                        attributeValue.setDescription(attributeValueDto.getDescription());
                        attributeValue.setAttributeProduct(attributeProduct); // связываем с атрибутом
                        attributeValueService.saveAttributeValue(attributeValue);
                        return attributeValue;
                    }).collect(Collectors.toList());
                    attributeProduct.setAttributeValues(attributeValues);
                }


                attributeProductService.saveAttributeProduct(attributeProduct);
                return attributeProduct;
            }).collect(Collectors.toList());


            product.setAttributeProducts(attributeProducts);
        }


        saveProduct(product); // обновляем продукт с новыми атрибутами

        log.info("Product created successfully with id: {}", product.getId());
        return product;
    }




    @Override
    public void updateProductFromDto(Long id, ProductDto productDto) {
        log.info("Updating product from dto with id: {}", id);
        productRepository.findById(id).map(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setStatus(Product.Status.valueOf(productDto.getStatus()));
            product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory())));

            // Удаление AttributeProduct, которые отсутствуют в productDto
            if (product.getAttributeProducts() != null) {
                product.getAttributeProducts().removeIf(attributeProduct -> {
                    boolean existsInDto = productDto.getAttributeProducts() != null && productDto.getAttributeProducts().stream()
                            .anyMatch(attributeProductDto -> attributeProductDto.getId().equals(attributeProduct.getId()));
                    if (!existsInDto) {
                        attributeProduct.getAttributeValues().forEach(attributeValueService::deleteAttributeValue);
                        attributeProductService.deleteAttributeProduct(attributeProduct);
                    }
                    return !existsInDto;
                });
            }

            // Добавление или обновление AttributeProduct и их значений
            if (productDto.getAttributeProducts() != null) {
                productDto.getAttributeProducts().forEach(attributeProductDto -> {
                    Optional<AttributeProduct> existingAttributeProduct = product.getAttributeProducts() != null ?
                            product.getAttributeProducts().stream()
                                    .filter(attributeProduct -> attributeProduct.getId().equals(attributeProductDto.getId()))
                                    .findFirst() : Optional.empty();

                    if (existingAttributeProduct.isPresent()) {
                        // Обновление существующего AttributeProduct
                        AttributeProduct attributeProduct = existingAttributeProduct.get();
                        attributeProduct.setName(attributeProductDto.getName());
                        attributeProduct.setType(AttributeProduct.TypeAttribute.valueOf(attributeProductDto.getType()));
                        attributeProductService.saveAttributeProduct(attributeProduct);

                        // Удаление AttributeValue, которые отсутствуют в attributeProductDto
                        if (attributeProduct.getAttributeValues() != null) {
                            attributeProduct.getAttributeValues().removeIf(attributeValue -> {
                                boolean existsInDto = attributeProductDto.getAttributeValues() != null && attributeProductDto.getAttributeValues().stream()
                                        .anyMatch(attributeValueDto -> attributeValueDto.getId().equals(attributeValue.getId()));
                                if (!existsInDto) {
                                    attributeValueService.deleteAttributeValue(attributeValue);
                                }
                                return !existsInDto;
                            });
                        }

                        // Добавление или обновление AttributeValue
                        if (attributeProductDto.getAttributeValues() != null) {
                            attributeProductDto.getAttributeValues().forEach(attributeValueDto -> {
                                Optional<AttributeValue> existingAttributeValue = attributeProduct.getAttributeValues() != null ?
                                        attributeProduct.getAttributeValues().stream()
                                                .filter(attributeValue -> attributeValue.getId().equals(attributeValueDto.getId()))
                                                .findFirst() : Optional.empty();

                                if (existingAttributeValue.isPresent()) {
                                    // Обновление существующего AttributeValue
                                    AttributeValue attributeValue = existingAttributeValue.get();
                                    attributeValue.setName(attributeValueDto.getName());
                                    attributeValue.setPrice(attributeValueDto.getPrice());
                                    attributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                                    attributeValue.setDescription(attributeValueDto.getDescription());
                                    attributeValueService.saveAttributeValue(attributeValue);
                                } else {
                                    // Добавление нового AttributeValue
                                    AttributeValue newAttributeValue = new AttributeValue();
                                    newAttributeValue.setName(attributeValueDto.getName());
                                    newAttributeValue.setPrice(attributeValueDto.getPrice());
                                    newAttributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                                    newAttributeValue.setDescription(attributeValueDto.getDescription());
                                    newAttributeValue.setAttributeProduct(attributeProduct); // связываем с атрибутом
                                    if (attributeProduct.getAttributeValues() == null) {
                                        attributeProduct.setAttributeValues(new ArrayList<>());
                                    }
                                    attributeProduct.getAttributeValues().add(newAttributeValue);
                                    attributeValueService.saveAttributeValue(newAttributeValue);
                                }
                            });
                        }

                    } else {
                        // Добавление нового AttributeProduct
                        AttributeProduct newAttributeProduct = new AttributeProduct();
                        newAttributeProduct.setName(attributeProductDto.getName());
                        newAttributeProduct.setType(AttributeProduct.TypeAttribute.valueOf(attributeProductDto.getType()));
                        newAttributeProduct.setProduct(product); // связываем с продуктом
                        attributeProductService.saveAttributeProduct(newAttributeProduct);

                        if (attributeProductDto.getAttributeValues() != null) {
                            List<AttributeValue> attributeValues = attributeProductDto.getAttributeValues().stream().map(attributeValueDto -> {
                                AttributeValue newAttributeValue = new AttributeValue();
                                newAttributeValue.setName(attributeValueDto.getName());
                                newAttributeValue.setPrice(attributeValueDto.getPrice());
                                newAttributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                                newAttributeValue.setDescription(attributeValueDto.getDescription());
                                newAttributeValue.setAttributeProduct(newAttributeProduct); // связываем с атрибутом
                                attributeValueService.saveAttributeValue(newAttributeValue);
                                return newAttributeValue;
                            }).collect(Collectors.toList());
                            newAttributeProduct.setAttributeValues(attributeValues);
                        }

                        if (product.getAttributeProducts() == null) {
                            product.setAttributeProducts(new ArrayList<>());
                        }
                        product.getAttributeProducts().add(newAttributeProduct);
                    }
                });
            }

            productRepository.save(product);
            return product;
        }).orElseThrow(() -> {
            log.error("Product not found");
            return new RuntimeException("Product not found");
        });
    }


    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
        log.info("Product deleted successfully");
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        log.info("Product deleted successfully by id: {}", id);
    }

    @Override
    public Page<Product> findAllProducts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        log.info("Get products with pageable: {}", pageable);
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findProductsByRequest(int page, int pageSize, String search) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Product> specification = new ProductSpecification(search);
        log.info("Get products with pageable and specification: {}", search);
        return productRepository.findAll(specification, pageable);
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
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findProductByCategory_Id(categoryId);
    }

    @Override
    public List<ProductDto> getProductsDtoByCategory(Long categoryId) {
        return productMapper.toDtoList(getProductsByCategory(categoryId));
    }

    @Override
    public List<TopProduct> getTop3Products() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> results = productRepository.findTop3Products(pageable);

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

}
