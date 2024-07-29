package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Customer c")
    Integer countAllCustomers();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.registrationDate >= CURRENT_DATE")
    Integer countTodayNewCustomers();

}