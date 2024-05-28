package com.spacelab.coffeeapp.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    private String street;

    private String building;

    private String apartment;

    private LocalDateTime deliveryTime;

    private LocalDateTime actualDeliveryTime;

    private Double changeAmount;

    private DeliveryStatus status;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "order_id")
    private Orders orders;

    public enum DeliveryStatus {
        ORDER_ACCEPTED,
        IN_PROGRESS,
        DELIVERED,
        CANCELLED
    }
}