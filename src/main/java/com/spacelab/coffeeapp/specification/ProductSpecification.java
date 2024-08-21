package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecification {

    static Specification<Product> search(String searchValue) {
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

    static Specification<Product> byNotDeleted() {
        return (root, query, builder) ->
                builder.equal(root.get("deleted"), false);
    }

    static Specification<Product> byCategoryId(Long categoryId) {
        return (root, query, builder) ->
                builder.equal(root.get("category").get("id"), categoryId);
    }


}
