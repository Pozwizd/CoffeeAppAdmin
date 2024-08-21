package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.AttributeValue;
import com.spacelab.coffeeapp.validators.product.ValidDiscountPrice;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Size(min = 3, max = 50, message = "Описание должно быть от 3 до 50 символов")
    String description;
    @Min(value = 0, message = "Цена не может быть отрицательной")
    Double price;
    @Min(value = 0, message = "Цена со скидкой не может быть отрицательной")
    Double priceWithDiscount;
}