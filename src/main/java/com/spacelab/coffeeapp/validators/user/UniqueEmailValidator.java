package com.spacelab.coffeeapp.validators.user;


import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, UserDto> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {
        if (userDto == null || userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            return true;
        }

        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(userDto.getId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Email уже используется")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
