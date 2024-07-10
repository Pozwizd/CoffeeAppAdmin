package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {


}