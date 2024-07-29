package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    // Методы для топ-4 продуктов за указанный период
    @Query("SELECT oi.product.id, COUNT(oi.id) as product_count " +
            "FROM OrderItem oi JOIN oi.order o " +
            "WHERE o.dateTimeOfCreate BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.id " +
            "ORDER BY product_count DESC")
    List<Object[]> findTop4Products(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);

    // Метод для подсчета количества продаж продукта по месяцам
    @Query("SELECT FUNCTION('YEAR', o.dateTimeOfCreate) as year, " +
            "FUNCTION('MONTH', o.dateTimeOfCreate) as month, " +
            "COUNT(oi.id) as product_count " +
            "FROM OrderItem oi JOIN oi.order o " +
            "WHERE oi.product.id = :productId " +
            "AND o.dateTimeOfCreate BETWEEN :startDate AND :endDate " +
            "GROUP BY year, month")
    List<Object[]> countProductSalesByMonth(@Param("productId") Long productId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);
}