package com.spacelab.coffeeapp.validators.user;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email уже используется";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
