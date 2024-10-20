package com.spacelab.coffeeapp.validators.customer.phoneNumberValidation;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.repository.CustomerRepository;
import com.spacelab.coffeeapp.service.CustomerService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;


import java.util.Optional;

@AllArgsConstructor
public class FieldPhoneUniqueValidator implements ConstraintValidator<FieldPhoneUnique, CustomerDto> {

    private final CustomerService customerService;

    @Override
    public void initialize(FieldPhoneUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(CustomerDto customerDto, ConstraintValidatorContext constraintValidatorContext) {

        if (customerDto == null || customerDto.getPhoneNumber() == null || customerDto.getPhoneNumber().isEmpty()) {
            return true;
        }

        Customer existingUser = customerService.getCustomerByPhoneNumber(customerDto.getPhoneNumber());

        if (existingUser != null && !existingUser.getId().equals(customerDto.getId())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Телефон уже используется")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
