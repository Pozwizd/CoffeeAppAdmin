package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.AttributeProduct;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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
    String type;
    List<AttributeValueDto> attributeValues;
}