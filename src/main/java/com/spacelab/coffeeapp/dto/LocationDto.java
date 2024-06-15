package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Location;
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
    String city;
    String latitude;
    String longitude;
    String street;
    String building;
}