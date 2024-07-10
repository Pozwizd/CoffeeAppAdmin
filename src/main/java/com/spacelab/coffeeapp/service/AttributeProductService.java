package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttributeProductService {

    void saveAttributeProduct(AttributeProduct attributeProduct);
    AttributeProduct getAttributeProduct(Long id);
    List<AttributeProduct> getAllAttributeProducts();
    void updateAttributeProduct(Long id, AttributeProduct attributeProduct);
    void deleteAttributeProduct(AttributeProduct attributeProduct);
    void deleteAttributeProduct(Long id);

    Long findMaxId();
}
