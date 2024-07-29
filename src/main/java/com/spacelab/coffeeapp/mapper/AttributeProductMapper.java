package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttributeProductMapper {

    public AttributeProductDto toDto(AttributeProduct attributeProduct) {
        AttributeProductDto attributeProductDto = new AttributeProductDto();
        attributeProductDto.setId(attributeProduct.getId());
        attributeProductDto.setName(attributeProduct.getName());
        attributeProductDto.setType(attributeProduct.getType().toString());
        List<AttributeValueDto> attributeValueDtos = new ArrayList<>();
        for (AttributeValue attributeValue : attributeProduct.getAttributeValues()) {
            AttributeValueDto attributeValueDto = new AttributeValueDto();
            attributeValueDto.setId(attributeValue.getId());
            attributeValueDto.setName(attributeValue.getName());
            attributeValueDto.setDescription(attributeValue.getDescription());
            attributeValueDto.setPrice(attributeValue.getPrice());
            attributeValueDto.setPriceWithDiscount(attributeValue.getPriceWithDiscount());
            attributeValueDtos.add(attributeValueDto);
        }
        attributeProductDto.setAttributeValues(attributeValueDtos);

        return attributeProductDto;
    }

    public List<AttributeProductDto> toDtoList(List<AttributeProduct> attributeProducts) {
        return attributeProducts
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
