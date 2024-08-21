package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link AttributeProduct}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeProductDto implements Serializable {

    Long id;

    @NotEmpty(message = "Название не может быть пустым")
    String name;

    @NotEmpty(message = "Укажите тип")
    String type;

    @NotEmpty(message = "Укажите значение")
    List<Long> productId;

    Boolean status;

    List<AttributeValueDto> attributeValues;
}