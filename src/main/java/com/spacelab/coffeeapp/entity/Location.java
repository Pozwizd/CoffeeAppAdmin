package com.spacelab.coffeeapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;

    private String latitude;
    private String longitude;

    private String street;
    private String building;
}
