package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findAll(Specification<Category> specification, Pageable pageable);

    List<Category> findAll(Specification<Category> categorySpecification);
}