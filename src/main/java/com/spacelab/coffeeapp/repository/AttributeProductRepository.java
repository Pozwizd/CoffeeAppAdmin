package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeProductRepository extends JpaRepository<AttributeProduct, Long> {
}