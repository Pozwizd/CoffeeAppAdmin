package com.spacelab.coffeeapp.mapper;


import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeValueMapper {

    public AttributeValueDto toDto(AttributeValue attributeValue) {
        AttributeValueDto attributeValueDto = new AttributeValueDto();
        attributeValueDto.setId(attributeValue.getId());
        attributeValueDto.setName(attributeValue.getName());
        attributeValueDto.setDescription(attributeValue.getDescription());
        attributeValueDto.setPrice(attributeValue.getPrice());
        attributeValueDto.setPriceWithDiscount(attributeValue.getPriceWithDiscount());
        return attributeValueDto;
    }

    public AttributeValue toEntity(AttributeValueDto attributeValueDto) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(attributeValueDto.getId());
        attributeValue.setName(attributeValueDto.getName());
        attributeValue.setDescription(attributeValueDto.getDescription());
        attributeValue.setPrice(attributeValueDto.getPrice());
        attributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
        return attributeValue;
    }

    public List<AttributeValueDto> toDtoList(List<AttributeValue> allAttributeValues) {
        return allAttributeValues
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
