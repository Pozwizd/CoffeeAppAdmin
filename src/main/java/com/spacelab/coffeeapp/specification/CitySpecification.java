package com.spacelab.coffeeapp.specification;

import org.springframework.data.jpa.domain.Specification;

public interface CitySpecification {

    static Specification<String> search(String searchValue) {
        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("name"), "%" + searchValue + "%");
        };
    }
}
