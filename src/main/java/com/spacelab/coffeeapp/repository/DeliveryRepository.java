package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}