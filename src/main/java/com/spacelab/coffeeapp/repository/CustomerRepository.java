package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}