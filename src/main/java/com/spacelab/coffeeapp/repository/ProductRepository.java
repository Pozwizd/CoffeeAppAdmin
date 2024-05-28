package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}