package com.spacelab.coffeeapp.validators.customer.emailValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
@Constraint(validatedBy = FieldEmailUniqueValidator.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldEmailUnique {
    String message() default "Такай адрес электронной почты уже зарегистрирован";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
