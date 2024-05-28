package com.spacelab.coffeeapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime dateTimeOfCreate;

    private LocalDateTime dateTimeOfUpdate;

    private LocalDateTime dateTimeOfReady;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();


    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    private OrderStatus status;

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
}