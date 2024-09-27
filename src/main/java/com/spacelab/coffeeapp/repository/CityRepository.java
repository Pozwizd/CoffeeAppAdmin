package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

    City findCityByName(String name);

    Page<City> findAll(Specification<String> search, Pageable pageable);
}