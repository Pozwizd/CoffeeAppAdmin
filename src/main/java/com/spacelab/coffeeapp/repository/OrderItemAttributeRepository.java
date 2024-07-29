package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.OrderItem;
import com.spacelab.coffeeapp.entity.OrderItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

public interface OrderItemAttributeRepository extends JpaRepository<OrderItemAttribute, Long> {
    Iterator<OrderItemAttribute> findByOrderItem_Id(Long id);

    long deleteByOrderItem(OrderItem orderItem);

    Iterable<OrderItemAttribute> findOrderItemAttributeByOrderItem_Id(Long id);

    @Modifying
    @Query("delete from OrderItemAttribute where orderItem.id = ?1")
    void deleteByOrderItem_Id(Long id);
}