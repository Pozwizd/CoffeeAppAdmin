package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.repository.ProductRepository;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.AttributeValueService;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import com.spacelab.coffeeapp.specification.ProductSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImp implements ProductService {

    private final CategoryService categoryService;
    private final AttributeProductService attributeProductService;
    private final AttributeValueService attributeValueService;
    private final ProductRepository productRepository;

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
    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
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

        // Сначала сохраняем продукт, чтобы получить его ID
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
    public long countProducts() {
        return productRepository.count();
    }
}
