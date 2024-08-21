package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import com.spacelab.coffeeapp.entity.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public interface AttributeProductSpecification{
    static Specification<AttributeProduct> search(String searchValue) {
        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.isEmpty()) {
                return null;
            }

            String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerSearchValue)
            );
        };
    }

    static Specification<AttributeProduct> byNotDeleted(){
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }

    static Specification<AttributeProduct> byType(AttributeProduct.TypeAttribute type) {
        return (root, query, builder) ->
                builder.equal(root.get("type"), type);
    }

    static Specification<AttributeProduct> byProductId(Long productId) {
        return (root, query, builder) -> {
            Join<AttributeProduct, Product> product = root.join("products");
            return builder.equal(product.get("id"), productId);
        };
    }

}