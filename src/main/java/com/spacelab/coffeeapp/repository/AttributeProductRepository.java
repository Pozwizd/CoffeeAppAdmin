package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttributeProductRepository extends JpaRepository<AttributeProduct, Long> {

    @Query("SELECT MAX(id) FROM AttributeProduct")
    Long findMaxId();


    Page<AttributeProduct> findAll(Specification<AttributeProduct> specification, Pageable pageable);

    List<AttributeProduct> findAll(Specification<AttributeProduct> attributeProductSpecification);

    @Query("SELECT ap FROM AttributeProduct ap WHERE ap.id = (SELECT MAX(ap.id) FROM AttributeProduct ap)")
    AttributeProduct findLastAttributeProduct();
}