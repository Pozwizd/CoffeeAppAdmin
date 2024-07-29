package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link Product}
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    @Query("select max(id) from Product")
    Long findMaxId();

    Product findByCategory_Id(Long id);

    List<Product> findProductByCategory_Id(Long id);

    @Query("SELECT COUNT(oi) FROM OrderItem oi")
    Long countTotalOrderItems();

    @Query("SELECT p.name as name, SUM(oi.quantity) as totalQuantity FROM Product p " +
            "JOIN OrderItem oi ON p.id = oi.product.id " +
            "GROUP BY p.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTop3Products(Pageable pageable);



    @Query("SELECT p.id, p.name, SUM(oi.quantity) AS totalSales FROM OrderItem oi " +
            "JOIN oi.product p " +
            "JOIN oi.order o " +
            "WHERE o.dateTimeOfCreate >= :startDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY totalSales DESC")
    List<Object[]> findTopSellingProducts(@Param("startDate") LocalDate startDate, Pageable pageable);

}