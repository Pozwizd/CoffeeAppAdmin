package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class LocationSpecification implements Specification<Location> {
    private final String searchValue;

    public LocationSpecification(String searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchValue == null || searchValue.isEmpty()) {
            return null;
        }

        String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("street")), lowerSearchValue)
        );
    }
}