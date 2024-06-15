package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.CustomerStatus;
import com.spacelab.coffeeapp.entity.Language;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

/**
 * DTO for {@link Customer}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable {
    Long id;
    String name;
    String email;
    Date dateOfBirth;
    String address;
    String phoneNumber;
    String language;
    String status;
}