package com.spacelab.coffeeapp.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor // Создает конструктор со всеми полями
public class ProductSalesDTO {
    private String productName;
    private int month;
    private Long purchaseCount; // Изменили на Long
}