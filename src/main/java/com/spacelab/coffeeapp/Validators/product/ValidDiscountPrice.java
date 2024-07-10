package com.spacelab.coffeeapp.Validators.product;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// Указываем, что эта аннотация будет использоваться для валидации с помощью PriceWithDiscountValidator
@Constraint(validatedBy = PriceWithDiscountValidator.class)
// Аннотацию можно применять к классам (типам)
@Target({ ElementType.TYPE })
// Аннотация будет доступна во время выполнения программы
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiscountPrice {
    // Сообщение об ошибке по умолчанию
    String message() default "Цена со скидкой не может быть больше обычной цены";

    // Группы валидации (необязательно)
    Class<?>[] groups() default {};

    // Дополнительная информация о полезной нагрузке (необязательно)
    Class<? extends Payload>[] payload() default {};

    // Имя поля с обычной ценой
    String priceField();

    // Имя поля с ценой со скидкой
    String discountPriceField();
}
