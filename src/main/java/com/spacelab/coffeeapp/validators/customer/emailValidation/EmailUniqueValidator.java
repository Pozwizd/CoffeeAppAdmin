package com.spacelab.coffeeapp.validators.customer.emailValidation;

import com.spacelab.coffeeapp.dto.CustomerDto;
import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.service.CustomerService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, CustomerDto> {

    private final CustomerService customerService;


    @Override
    public void initialize(EmailUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(CustomerDto customerProfileRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (customerProfileRequest == null || customerProfileRequest.getEmail() == null || customerProfileRequest.getEmail().isEmpty()) {
            return true;
        }

        Customer existingUser = customerService.getCustomerByEmail(customerProfileRequest.getEmail());
        if (existingUser != null && !existingUser.getId().equals(customerProfileRequest.getId())) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Email уже используется")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }


}
