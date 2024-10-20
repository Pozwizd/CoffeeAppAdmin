package com.spacelab.coffeeapp.validators.customer.emailValidation;

import com.spacelab.coffeeapp.service.CustomerService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class FieldEmailUniqueValidator implements ConstraintValidator<FieldEmailUnique, String> {
    private final CustomerService customerService;

    public FieldEmailUniqueValidator(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return customerService.getCustomerByEmail(email) == null;
    }
}
