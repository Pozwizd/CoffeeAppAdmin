package com.spacelab.coffeeapp.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Автоматически генерирует конструктор для всех финальных полей
public enum Permission { // Создаём перечисление (enum) для разрешений (permissions)

    ADMIN_READ("admin:read"), // Разрешение на чтение данных администратора
    ADMIN_UPDATE("admin:update"), // Разрешение на обновление данных администратора
    ADMIN_CREATE("admin:create"), // Разрешение на создание данных администратора
    ADMIN_DELETE("admin:delete"), // Разрешение на удаление данных администратора
    MANAGER_READ("management:read"), // Разрешение на чтение данных менеджера
    MANAGER_UPDATE("management:update"), // Разрешение на обновление данных менеджера
    MANAGER_CREATE("management:create"), // Разрешение на создание данных менеджера
    MANAGER_DELETE("management:delete"); // Разрешение на удаление данных менеджера

    @Getter
    private final String permission; // Поле для хранения строки, представляющей разрешение
}
