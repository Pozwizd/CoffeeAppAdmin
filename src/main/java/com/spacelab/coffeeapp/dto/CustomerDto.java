package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.CustomerStatus;
import com.spacelab.coffeeapp.entity.Language;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов")
    String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Некорректная почта")
    String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @Size(min = 3, max = 50, message = "Адрес должен быть от 3 до 50 символов")
    String address;

    @Size(min = 3, max = 50, message = "Номер телефона должен быть от 3 до 50 символов")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "Некорректный номер телефона")
    String phoneNumber;

    Language language;

    String status;
}