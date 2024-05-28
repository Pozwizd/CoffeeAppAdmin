package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}