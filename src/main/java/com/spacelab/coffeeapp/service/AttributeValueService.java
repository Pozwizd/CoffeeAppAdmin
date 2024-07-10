package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.AttributeValue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttributeValueService {

    void saveAttributeValue(AttributeValue attributeValue);

    AttributeValue getAttributeValue(Long id);

    List<AttributeValue> getAllAttributeValues();

    void updateAttributeValue(Long id, AttributeValue attributeValue);

    void deleteAttributeValue(AttributeValue attributeValue);

}
