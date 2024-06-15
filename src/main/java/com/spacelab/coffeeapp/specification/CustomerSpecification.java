package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Location;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecification implements Specification<Customer> {

    private final String searchValue;

    public CustomerSpecification(String searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchValue == null || searchValue.isEmpty()) {
            return null;
        }

        String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerSearchValue),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerSearchValue),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), lowerSearchValue),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), lowerSearchValue)
        );
    }
}
