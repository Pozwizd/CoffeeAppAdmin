package com.spacelab.coffeeapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue
    private Long id;

    private int quantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<OrderItemAttribute> orderItemAttributes = new ArrayList<>();

    public double getTotalAmount() {
        return quantity * orderItemAttributes.stream()
                .mapToDouble(attr -> attr.getAttributeValue().getPrice())
                .sum();
    }

}
