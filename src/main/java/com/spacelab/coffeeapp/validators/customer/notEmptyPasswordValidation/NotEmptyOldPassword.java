package com.spacelab.coffeeapp.validators.customer.notEmptyPasswordValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = NotEmptyOldPasswordValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyOldPassword {
    String oldPassword();
    String newPassword();

    String message() default "Поле не может быть пустым";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
