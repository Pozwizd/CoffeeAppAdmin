package com.spacelab.coffeeapp.validators.customer.phoneNumberValidation;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;


import java.util.Optional;

public class PhoneNumberUniqueValidator implements ConstraintValidator<PhoneNumberUnique, Object> {
    private final CustomerRepository customerRepository;

    public PhoneNumberUniqueValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    private String id;
    private String phoneNumber;

    @Override
    public void initialize(PhoneNumberUnique constraintAnnotation) {
        this.id = constraintAnnotation.id();
        this.phoneNumber = constraintAnnotation.phoneNumber();
    }

    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);
        Object phoneValue = new BeanWrapperImpl(s).getPropertyValue(phoneNumber);
        Optional<Customer> user = customerRepository.findByPhoneNumber(phoneValue.toString());
        if(user.isPresent() && !user.get().getId().equals((Long) idValue)){
            return false;
        }
        return true;
    }
}
