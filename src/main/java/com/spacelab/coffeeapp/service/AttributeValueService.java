package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AttributeValueService {

    AttributeValue saveAttributeValue(AttributeValue attributeValue);

    void saveAttributeValueFromDto(AttributeValueDto attributeValueDto);

    Optional<AttributeValue> getAttributeValue(Long id);

    AttributeValueDto getAttributeValueDto(Long id);

    List<AttributeValue> getAllAttributeValues();

    List<AttributeValueDto> getAllAttributeValuesDto();
    List<AttributeValueDto> getAllAttributeValuesDtoByAttributeId(Long attributeId);

    void updateAttributeValue(Long id, AttributeValue attributeValue);

    void updateAttributeValueFromDto(Long id, AttributeValueDto attributeValueDto);

    void deleteAttributeValue(AttributeValue attributeValue);

    void deleteAttributeValueById(Long id);

    List<AttributeValue> findByAttributeProduct(Long id);
}
