package com.spacelab.coffeeapp.validators.customer.validateOldPassword;

import com.spacelab.coffeeapp.entity.Customer;
import com.spacelab.coffeeapp.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.password.PasswordEncoder;


public class OldPasswordMatchingValidator implements ConstraintValidator<OldPasswordMatching,Object> {
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OldPasswordMatchingValidator(CustomerRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String id;
    private String oldPassword;
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        Object old = new BeanWrapperImpl(s).getPropertyValue(oldPassword);
        if(old == null || old.equals("")){
            return true;
        }
        Object idValue = new BeanWrapperImpl(s).getPropertyValue(id);

        Customer user = userRepository.findById((Long)idValue).orElseThrow(()-> new EntityNotFoundException("User was not found by id "+idValue));
        return passwordEncoder.matches(old.toString(),user.getPassword());
    }

    @Override
    public void initialize(OldPasswordMatching matching) {
        this.id = matching.id();
        this.oldPassword = matching.oldPassword();
    }
}
