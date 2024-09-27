package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

public interface CustomerSpecification {

    static Specification<Customer> search(String searchValue) {
        return (root, query, criteriaBuilder) -> {
            if (searchValue == null || searchValue.isEmpty()) {
                return null;
            }
            String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerSearchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerSearchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), lowerSearchValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), lowerSearchValue));
        };
    }

    static Specification<Customer> byNotDeleted() {
        return (root, query, builder) -> builder.equal(root.get("deleted"), false);
    }
}
