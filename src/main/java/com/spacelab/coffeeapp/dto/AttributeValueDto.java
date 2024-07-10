package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.Validators.product.ValidDiscountPrice;
import com.spacelab.coffeeapp.entity.AttributeValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link AttributeValue}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidDiscountPrice(priceField = "price", discountPriceField = "priceWithDiscount")
public class AttributeValueDto implements Serializable {
    Long id;
    @Size(min = 3, max = 50, message = "Наименование должно быть от 3 до 50 символов")
    @NotEmpty(message = "Наименование не может быть пустым")
    String name;
    String description;
    Double price;
    Double priceWithDiscount;
}