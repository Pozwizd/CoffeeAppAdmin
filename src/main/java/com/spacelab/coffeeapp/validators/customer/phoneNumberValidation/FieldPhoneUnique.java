package com.spacelab.coffeeapp.validators.customer.phoneNumberValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = FieldPhoneUniqueValidator.class)
@Target({TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldPhoneUnique {
    String message() default "Такой номер телефона уже существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
