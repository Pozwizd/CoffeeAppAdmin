package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.Location;
import org.springframework.data.jpa.domain.Specification;

public interface LocationSpecification {

    static Specification<Location> search(String searchValue) {

        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.isEmpty()) {
                return null;
            }
            String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("street")), lowerSearchValue)
            );
        };
    }
}
