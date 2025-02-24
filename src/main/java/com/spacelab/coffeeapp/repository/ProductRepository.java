package com.spacelab.coffeeapp.repository;

import com.spacelab.coffeeapp.dto.ProductSalesDTO;
import com.spacelab.coffeeapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("SELECT p.name as name, COUNT(oi.id) as purchaseCount FROM Product p " +
            "JOIN OrderItem oi ON p.id = oi.product.id " +
            "GROUP BY p.name " +
            "ORDER BY COUNT(oi.id) DESC")
    List<Object[]> findTopProductsAndFrequency(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "JOIN OrderItem oi ON p.id = oi.product.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(oi.id) DESC")
    List<Product> findTopProducts(Pageable pageable);

    @Query("SELECT p.id, p.name, SUM(oi.quantity) AS totalSales FROM OrderItem oi " +
            "JOIN oi.product p " +
            "JOIN oi.order o " +
            "WHERE o.dateTimeOfCreate >= :startDate " +
            "GROUP BY p.id, p.name " +
            "ORDER BY totalSales DESC")
    List<Object[]> findTopSellingProducts(@Param("startDate") LocalDate startDate, Pageable pageable);

    List<Product> findAll(Specification<Product> productSpecification);

    @Query("SELECT p.name, EXTRACT(MONTH FROM o.dateTimeOfCreate) AS month, COUNT(oi.id) AS purchaseCount " +
            "FROM Product p " +
            "JOIN OrderItem oi ON p.id = oi.product.id " +
            "JOIN Order o ON o.id = oi.order.id " +
            "WHERE o.dateTimeOfCreate >= :startDate " +
            "GROUP BY p.name, EXTRACT(MONTH FROM o.dateTimeOfCreate) " +
            "ORDER BY COUNT(oi.id) DESC")
    List<Object[]> findTopProductsSalesByMonth(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    @Query("SELECT new ProductSalesDTO(p.name, EXTRACT(MONTH FROM o.dateTimeOfCreate), COUNT(oi.id)) " +
            "FROM Product p " +
            "JOIN OrderItem oi ON p.id = oi.product.id " +
            "JOIN Order o ON o.id = oi.order.id " +
            "WHERE o.dateTimeOfCreate >= :startDate " +
            "GROUP BY p.name, EXTRACT(MONTH FROM o.dateTimeOfCreate) " +
            "ORDER BY COUNT(oi.id) DESC")
    List<ProductSalesDTO> findTopProductsSalesByMonth1(@Param("startDate") LocalDateTime startDate, Pageable pageable);

}