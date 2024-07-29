package com.spacelab.coffeeapp.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


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

    private String subDoor;

    private String apartment;

    private LocalDate deliveryDate;

    private LocalTime deliveryTime;

    private Double changeAmount;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public enum DeliveryStatus {
        ORDER_ACCEPTED,
        IN_PROGRESS,
        DELIVERED,
        CANCELLED
    }
}