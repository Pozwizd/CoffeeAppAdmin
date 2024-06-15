package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Location;
import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    Page<Location> findAll(Specification<Location> specification, Pageable pageable);

}