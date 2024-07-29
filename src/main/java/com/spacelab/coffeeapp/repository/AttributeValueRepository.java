package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    List<AttributeValue> findByAttributeProduct_Id(Long id);

    List<AttributeValue> findAttributeValueByAttributeProduct_Id(Long id);
}
