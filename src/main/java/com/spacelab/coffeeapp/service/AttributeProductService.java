package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AttributeProductService {

    void saveAttributeProduct(AttributeProduct attributeProduct);
    Optional<AttributeProduct> getAttributeProduct(Long id);
    AttributeProductDto getAttributeProductDto(Long id);

    List<AttributeProduct> getAllAttributeProducts();

    List<AttributeProduct> findByProduct(Long productId);
    List<AttributeProductDto> getAllAttributesDtoByProduct(String productId);
    void updateAttributeProduct(Long id, AttributeProduct attributeProduct);
    boolean deleteAttributeProduct(AttributeProduct attributeProduct);
    boolean deleteAttributeProduct(Long id);

    Long findMaxId();

    Page<AttributeProductDto> findAttributesDtoByRequest(int page, int size, String search);

    Boolean saveAttributeProductFromDto(AttributeProductDto attributeProductDto);

    List<AttributeProductDto> getAttributesByProduct(Long Long);
}
