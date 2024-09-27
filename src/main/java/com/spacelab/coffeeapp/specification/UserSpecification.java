package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.jpa.domain.Specification;

public interface UserSpecification {

    static Specification<User> search(String searchValue) {

        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.isEmpty()) {
                return null;
            }

            String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerSearchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerSearchValue)
            );
        };
    }
}
