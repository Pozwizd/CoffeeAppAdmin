package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttributeProductRepository extends JpaRepository<AttributeProduct, Long> {

    @Query("SELECT MAX(id) FROM AttributeProduct")
    Long findMaxId();

    List<AttributeProduct> findAttributeProductByProduct_Id(Long id);
}