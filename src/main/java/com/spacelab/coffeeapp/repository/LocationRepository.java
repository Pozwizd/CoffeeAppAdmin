package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}