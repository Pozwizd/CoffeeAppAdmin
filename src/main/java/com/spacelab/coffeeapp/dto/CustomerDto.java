package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.entity.Language;
import com.spacelab.coffeeapp.validators.customer.emailValidation.EmailUnique;
import com.spacelab.coffeeapp.validators.customer.emailValidation.FieldEmailUnique;
import com.spacelab.coffeeapp.validators.customer.phoneNumberValidation.FieldPhoneUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Customer}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EmailUnique
@FieldPhoneUnique
public class CustomerDto implements Serializable {
    Long id;
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов")
    String name;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(max=100, message = "Размер поля должен быть не более 50 символов")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Неверный формат email")
    String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @Size(min = 3, max = 50, message = "Адрес должен быть от 3 до 50 символов")
    String address;

    @NotEmpty(message = "Поле не может быть пустым")
    @Size(max=13, message = "Размер номера должен быть не более 13 символов")
    @Pattern(regexp = "\\+380(50|66|95|99|67|68|96|97|98|63|93|73)[0-9]{7}", message = "Неверный формат номера")

    String phoneNumber;

    Language language;

    String status;
}