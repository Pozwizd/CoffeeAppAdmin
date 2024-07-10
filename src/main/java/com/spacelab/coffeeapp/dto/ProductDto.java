package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    String description;
    Integer quantity;
    String status;
    String category;
    List<AttributeProductDto> attributeProducts;

}