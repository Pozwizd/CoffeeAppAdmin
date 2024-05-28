package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
}