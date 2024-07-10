package com.spacelab.coffeeapp.mapper;

import com.spacelab.coffeeapp.dto.AttributeProductDto;
import com.spacelab.coffeeapp.dto.AttributeValueDto;
import com.spacelab.coffeeapp.dto.ProductDto;
import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.entity.Product;
import com.spacelab.coffeeapp.service.CategoryService;
import com.spacelab.coffeeapp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductMapper {

    private final ProductService productService;
    private final CategoryService categoryService;

    public Page<ProductDto> toDtoListPage(Page<Product> productPage) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product l : productPage.getContent()) {
            productDtos.add(toDto(l));
        }
        return new PageImpl<>(productDtos, productPage.getPageable(), productPage.getTotalElements());
    }
//
//    public List<Location> toEntityListPage(List<LocationDto> locationList) {
//        List<Location> locationDtoList = new ArrayList<>();
//        for (LocationDto l : locationList) {
//            locationDtoList.add(toEntity(l));
//        }
//        return locationDtoList;
//    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setQuantity(product.getAttributeProducts().size());
        productDto.setCategory(product.getCategory().getId().toString());
        productDto.setStatus(product.getStatus().toString());
        List<AttributeProductDto> attributeProductDtos = new ArrayList<>();
        for (AttributeProduct attributeProduct : product.getAttributeProducts()) {
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
            attributeProductDtos.add(attributeProductDto);
        }
        productDto.setAttributeProducts(attributeProductDtos);
        return productDto;
    }

    public Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(categoryService.getCategory(Long.valueOf(productDto.getCategory())));
        product.setStatus(Product.Status.valueOf(productDto.getStatus()));
        List<AttributeProduct> attributeProducts = new ArrayList<>();
        if (productDto.getAttributeProducts() != null) {
            for (AttributeProductDto attributeProductDto : productDto.getAttributeProducts()) {
                AttributeProduct attributeProduct = new AttributeProduct();
                attributeProduct.setId(attributeProductDto.getId());
                attributeProduct.setName(attributeProductDto.getName());
                attributeProduct.setType(AttributeProduct.TypeAttribute.valueOf(attributeProductDto.getType()));
                for (AttributeValueDto attributeValueDto : attributeProductDto.getAttributeValues()) {
                    AttributeValue attributeValue = new AttributeValue();
                    attributeValue.setId(attributeValueDto.getId());
                    attributeValue.setName(attributeValueDto.getName());
                    attributeValue.setDescription(attributeValueDto.getDescription());
                    attributeValue.setPrice(attributeValueDto.getPrice());
                    attributeValue.setPriceWithDiscount(attributeValueDto.getPriceWithDiscount());
                    attributeProduct.getAttributeValues().add(attributeValue);
                }
                attributeProducts.add(attributeProduct);
            }
        }
        product.setAttributeProducts(attributeProducts);
        return product;
    }

}
