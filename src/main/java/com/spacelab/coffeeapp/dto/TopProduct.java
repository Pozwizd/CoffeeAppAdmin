package com.spacelab.coffeeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopProduct {
    private String name;
    private Double percentage;

    public TopProduct(String name, Double percentage) {
        this.name = name;
        this.percentage = percentage;
    }
}