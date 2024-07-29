package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COUNT(o) FROM Order o")
    Integer countAllOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.dateTimeOfCreate >= CURRENT_DATE")
    Integer countTodayOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DONE'")
    Long findAllDoneOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'DONE' AND o.dateTimeOfCreate >= CURRENT_DATE")
    Long findAllDoneOrdersToday();

}