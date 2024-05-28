package com.spacelab.coffeeapp.specification;

import com.spacelab.coffeeapp.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification implements Specification<User> {
    private final String searchValue;

    public UserSpecification(String searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public Predicate toPredicate(Root<User> root,CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (searchValue == null || searchValue.isEmpty()) {
            return null;
        }

        String lowerSearchValue = "%" + searchValue.toLowerCase() + "%";
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerSearchValue),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerSearchValue)
        );
    }
}