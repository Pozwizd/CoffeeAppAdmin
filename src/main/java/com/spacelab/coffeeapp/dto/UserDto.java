package com.spacelab.coffeeapp.dto;

import com.spacelab.coffeeapp.Validators.user.UniqueEmail;
import com.spacelab.coffeeapp.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueEmail
public class UserDto implements Serializable {
    Long id;
    @Size(message = "Имя должно быть от 2 до 100 символов", min = 2, max = 100)
    @NotBlank(message = "Имя обязательно")
    String name;

    @Pattern(regexp = "^(.+)@(.+)$", message = "Email должен быть в формате example@example.com")
    String email;

    @Size(message = "Пароль должен быть от 6 до 100 символов", min = 6, max = 100)
    @NotBlank(message = "Пароль обязателен")
    String password;

    @NotBlank(message = "Роль обязательна")
    String role;
}