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

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Customer c")
    Integer countAllCustomers();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.registrationDate >= CURRENT_DATE")
    Integer countTodayNewCustomers();

    @Query("SELECT COUNT(DISTINCT o.customer.id) FROM Order o WHERE o.dateTimeOfCreate >= :oneWeekAgo")
    Long countUniqueCustomersWithOrdersSince(LocalDateTime oneWeekAgo);

    Customer getCustomerByEmail(String email);

    Optional<Customer> findByPhoneNumber(String s);
}