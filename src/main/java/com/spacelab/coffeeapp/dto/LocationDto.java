package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Location;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Location}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto implements Serializable {

    Long id;

    @NotEmpty(message = "Пожалуйста, укажите город.")
    String city;

    @Pattern(regexp = "^-?\\d{1,3}\\.\\d+$", message = "Введите корректную широту в формате: -90.0 до 90.0")
    String latitude;

    @Pattern(regexp = "^-?\\d{1,3}\\.\\d+$", message = "Введите корректную долготу в формате: -180.0 до 180.0")
    String longitude;

    String street;

    @NotEmpty(message = "Пожалуйста, укажите номер здания.")
    String building;
}
