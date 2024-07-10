package com.spacelab.coffeeapp.Validators.product;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;


// Реализуем интерфейс ConstraintValidator для
// аннотации ValidDiscountPrice и типа Object (класс, к которому применяется аннотация)
public class PriceWithDiscountValidator
        implements ConstraintValidator<ValidDiscountPrice, Object> {

    // Поля для хранения имен полей и сообщения об ошибке
    private String priceField;
    private String discountPriceField;
    private String message;

    // Метод инициализации, который вызывается при создании валидатора
    @Override
    public void initialize(ValidDiscountPrice constraintAnnotation) {
        // Сохраняем имена полей и сообщение из аннотации
        this.priceField = constraintAnnotation.priceField();
        this.discountPriceField = constraintAnnotation.discountPriceField();
        this.message = constraintAnnotation.message();
    }

    // Основной метод валидации
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // Получаем поля по их именам с использованием рефлексии
            Field priceField = value.getClass().getDeclaredField(this.priceField);
            Field discountPriceField = value.getClass().getDeclaredField(this.discountPriceField);

            // Делаем поля доступными для чтения и записи
            priceField.setAccessible(true);
            discountPriceField.setAccessible(true);

            // Получаем значения полей из объекта
            Double price = (Double) priceField.get(value);
            Double discountPrice = (Double) discountPriceField.get(value);

            // Если одно из значений null, пропускаем проверку
            if (price == null || discountPrice == null) {
                return true; // не проверяем, если одно из значений null
            }

            // Проверяем условие: цена со скидкой не должна быть больше обычной цены
            boolean isValid = discountPrice <= price;

            // Если условие не выполнено, создаем сообщение об ошибке
            if (!isValid) {
                // Отключаем стандартное сообщение об ошибке
                context.disableDefaultConstraintViolation();
                // Строим новое сообщение об ошибке и привязываем его к полю с ценой со скидкой
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(this.discountPriceField)
                        .addConstraintViolation();
            }

            // Возвращаем результат проверки
            return isValid;
        } catch (Exception e) {
            // Обрабатываем возможные исключения (например, если поля не найдены)
            e.printStackTrace();
            return false;
        }
    }
}
