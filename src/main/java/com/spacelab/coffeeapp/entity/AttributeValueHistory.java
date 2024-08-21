package com.spacelab.coffeeapp.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValueHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String description;

    private Double price = 0.0;

    private Double priceWithDiscount = 0.0;

    private Boolean deleted = false;

    @ManyToOne(targetEntity = AttributeValueHistory.class,fetch = FetchType.LAZY)
    private AttributeValue attributeValue;
}
