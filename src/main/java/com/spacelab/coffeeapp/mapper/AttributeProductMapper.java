package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        attributeProductDto.setStatus(attributeProduct.getStatus());
        attributeProductDto.setProductId(attributeProduct.getProducts().stream().map(Product::getId).collect(Collectors.toList()));
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

    public Page<AttributeProductDto> toDtoListPage(Page<AttributeProduct> attributeProductPage) {
        List<AttributeProductDto> attributeProductDtos = new ArrayList<>();
        for (AttributeProduct l : attributeProductPage.getContent()) {
            attributeProductDtos.add(toDto(l));
        }
        return new PageImpl<>(attributeProductDtos, attributeProductPage.getPageable(), attributeProductPage.getTotalElements());
    }

}
