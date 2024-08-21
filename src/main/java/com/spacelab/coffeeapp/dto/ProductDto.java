package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Product}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable {

    Long id;

    @NotEmpty(message = "Название не может быть пустым")
    String name;
    @NotEmpty(message = "Описание не может быть пустым")
    @Size(min = 3, max = 250, message = "Описание должно быть от 3 до 50 символов")
    String description;
    Double price;
    Double priceWithDiscount;
    @Min(value = 0, message = "Количество не может быть отрицательным")
    Integer quantity;
    String status;
    String category;
    List<AttributeProductDto> attributeProducts;

}