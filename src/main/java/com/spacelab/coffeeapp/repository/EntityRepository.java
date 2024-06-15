package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Entity2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends JpaRepository<Entity2, Long> {
    Page<Entity2> findByField1ContainingIgnoreCaseOrField2ContainingIgnoreCaseOrField3ContainingIgnoreCase(String field1, String field2, String field3, Pageable pageable);
}
