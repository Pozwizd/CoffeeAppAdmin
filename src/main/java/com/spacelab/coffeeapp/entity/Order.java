package com.spacelab.coffeeapp.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'TT'HH:mm")
    private LocalDateTime dateTimeOfCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'TT'HH:mm")
    private LocalDateTime dateTimeOfUpdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'TT'HH:mm")
    private LocalDateTime dateTimeOfReady;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private double totalAmount = 0;

    public enum OrderStatus {
        NEW,
        PAYMENT_WAITING,
        IN_PROGRESS,
        DELIVERING,
        DONE,
        CANCELLED
    }

    public enum Payment {
        CASH,
        CARD
    }

    @PrePersist
    @PreUpdate
    @PostPersist
    @PostConstruct
    @PostLoad
    public void updateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getTotalAmount)
                .sum();
    }
}
