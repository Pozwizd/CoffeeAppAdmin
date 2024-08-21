package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.mapper.AttributeProductMapper;
import com.spacelab.coffeeapp.repository.AttributeProductRepository;
import com.spacelab.coffeeapp.service.AttributeProductService;
import com.spacelab.coffeeapp.service.AttributeValueService;
import com.spacelab.coffeeapp.service.ProductService;
import com.spacelab.coffeeapp.specification.AttributeProductSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AttributeProductServiceImp implements AttributeProductService {

    private final AttributeProductRepository attributeProductRepository;
    private final AttributeValueService attributeValueService;
    private final ProductService productService;
    private final AttributeProductMapper attributeProductMapper;


    @Override
    public void saveAttributeProduct(AttributeProduct attributeProduct) {
        attributeProductRepository.save(attributeProduct);
        log.info("AttributeProduct saved successfully");
    }

    @Override
    public Optional<AttributeProduct> getAttributeProduct(Long id) {
        log.info("Fetching attributeProduct with id: {}", id);
        return attributeProductRepository.findById(id);
    }

    @Override
    public AttributeProductDto getAttributeProductDto(Long id) {
        return attributeProductMapper.toDto(getAttributeProduct(id).get());
    }


    @Override
    public List<AttributeProduct> getAllAttributeProducts() {
        log.info("Fetching all attributeProducts");
        return attributeProductRepository.findAll();
    }

    @Override
    public List<AttributeProduct> findByProduct(Long productId) {
        return attributeProductRepository.findAll(AttributeProductSpecification.byProductId(productId));
    }

    @Override
    public List<AttributeProductDto> getAllAttributesDtoByProduct(String productId) {
        return attributeProductMapper.toDtoList(findByProduct(Long.valueOf(productId)));
    }


    @Override
    public void updateAttributeProduct(Long id, AttributeProduct attributeProduct) {
        log.info("Updating attributeProduct with id: {}", id);
        attributeProductRepository.findById(id).map(attributeProduct1 -> {
            attributeProduct1.setName(attributeProduct.getName());
            attributeProduct1.setType(attributeProduct.getType());
//            attributeProduct1.setProduct(attributeProduct.getProduct());
            attributeProduct1.setAttributeValues(attributeProduct.getAttributeValues());
            attributeProductRepository.save(attributeProduct1);
            return attributeProduct1;
        }).orElseThrow(() -> {
            throw new RuntimeException("AttributeProduct not found");
        });
        log.info("Update attributeProduct: {}", attributeProduct);
    }

    @Override
    public boolean deleteAttributeProduct(AttributeProduct externalAttributeProduct) {
        Optional<AttributeProduct> optionalAttributeProduct = attributeProductRepository.findById(externalAttributeProduct.getId());

        optionalAttributeProduct.ifPresentOrElse(attributeProduct -> {
            attributeProduct.setDeleted(true);
            attributeProductRepository.save(attributeProduct);
            log.info("AttributeProduct deleted with id: {}", externalAttributeProduct.getId());
        }, () -> {
            log.info("AttributeProduct not found with id: {}", externalAttributeProduct.getId());
        });

        return optionalAttributeProduct.isPresent();
    }

    @Override
    public boolean deleteAttributeProduct(Long id) {
        Optional<AttributeProduct> optionalAttributeProduct = attributeProductRepository.findById(id);

        optionalAttributeProduct.ifPresentOrElse(attributeProduct -> {
            attributeProduct.setDeleted(true);
            attributeProductRepository.save(attributeProduct);
            log.info("AttributeProduct deleted with id: {}", id);
        }, () -> {
            log.info("AttributeProduct not found with id: {}", id);
        });

        return optionalAttributeProduct.isPresent();
    }

    @Override
    public Long findMaxId() {
        return attributeProductRepository.findMaxId();
    }

    public Page<AttributeProduct> findAllAttributeProductsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attributeProductRepository.findAll(AttributeProductSpecification.byNotDeleted(), pageable);
    }

    public Page<AttributeProduct> findAllAttributeProductsPageByRequest(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Get products with pageable and specification: {}", search);
        return attributeProductRepository.findAll(AttributeProductSpecification.search(search).and(AttributeProductSpecification.byNotDeleted()), pageable);
    }

    @Override
    public Page<AttributeProductDto> findAttributesDtoByRequest(int page, int size, String search) {
        if (search.isEmpty()) {
            return attributeProductMapper.toDtoListPage(findAllAttributeProductsPage(page, size));
        } else {
            return attributeProductMapper.toDtoListPage(findAllAttributeProductsPageByRequest(page, size, search));
        }
    }

    @Override
    public Boolean saveAttributeProductFromDto(AttributeProductDto attributeProductDto) {
        Optional<AttributeProduct> existingAttributeProductOptional = attributeProductRepository.findById(attributeProductDto.getId());

        AttributeProduct attributeProduct = existingAttributeProductOptional.orElseGet(AttributeProduct::new);

        attributeProduct.setName(attributeProductDto.getName());
        attributeProduct.setType(AttributeProduct.TypeAttribute.valueOf(attributeProductDto.getType()));
        attributeProduct.setStatus(attributeProductDto.getStatus());

        List<Product> products = new ArrayList<>();
        AttributeProduct finalAttributeProduct = attributeProduct;
        attributeProductDto.getProductId().forEach(productId -> {
            Product product = productService.getProduct(productId).orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
            products.add(product);
            product.getAttributeProducts().add(finalAttributeProduct); // Добавляем атрибут текущему продукту
        });

        attributeProduct.setProducts(products);

        attributeProductRepository.save(attributeProduct);

        if (attributeProductDto.getId() == null) {
            attributeProduct = attributeProductRepository.findLastAttributeProduct();
        }

        if (existingAttributeProductOptional.isPresent()) {
            attributeProduct.getAttributeValues().clear();
        }

        if (attributeProductDto.getAttributeValues() != null && !attributeProductDto.getAttributeValues().isEmpty()) {
            for (AttributeValueDto attributeValueDto : attributeProductDto.getAttributeValues()) {
                AttributeValue attributeValue;
                if (attributeValueDto.getId() != null) {
                    attributeValue = attributeValueService.getAttributeValue(attributeValueDto.getId())
                            .orElseGet(AttributeValue::new);
                } else {
                    attributeValue = new AttributeValue();
                }

                attributeValue.setName(attributeValueDto.getName());
                attributeValue.setDescription(attributeValueDto.getDescription());
                attributeValue.setPrice(attributeValueDto.getPrice());
                attributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                attributeValue.setAttributeProduct(attributeProduct);
                attributeValueService.saveAttributeValue(attributeValue);
                attributeProduct.getAttributeValues().add(attributeValue);
            }
        }

        attributeProductRepository.save(attributeProduct);

        return true;
    }

    @Override
    public List<AttributeProductDto> getAttributesByProduct(Long Long) {
        return attributeProductMapper.toDtoList(findByProduct(Long));
    }
}
