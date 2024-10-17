package com.spacelab.coffeeapp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 3, max = 100, message = "Название должно содержать от 3 до 100 символов")
    private String name;
    private Integer itemsInCategory;
    private String status;
}
