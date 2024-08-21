package com.spacelab.coffeeapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String description;

    private Double price = 0.0;

    private Double priceWithDiscount = 0.0;
    
    @ManyToOne
    @JoinColumn(name = "attribute_product_id")
    private AttributeProduct attributeProduct;

    private Boolean deleted = false;

    @OneToMany
    private List<AttributeValueHistory> attributeValueHistory;

}
