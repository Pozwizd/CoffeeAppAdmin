package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public interface CategorySpecification {

    static Specification<Category> search(String searchValue) {
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
}
