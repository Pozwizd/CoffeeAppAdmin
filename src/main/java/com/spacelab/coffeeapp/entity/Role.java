package com.spacelab.coffeeapp.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.spacelab.coffeeapp.entity.Permission.*;

@RequiredArgsConstructor // Автоматически генерирует конструктор для всех финальных полей
public enum Role { // Создаём перечисление (enum) для ролей пользователей

  USER(Collections.emptySet()), // Роль пользователя без специальных прав
  ADMIN( // Роль администратора с полным набором прав
          Set.of(
                  ADMIN_READ, // Право на чтение данных администратора
                  ADMIN_UPDATE, // Право на обновление данных администратора
                  ADMIN_DELETE, // Право на удаление данных администратора
                  ADMIN_CREATE, // Право на создание данных администратора
                  MANAGER_READ, // Право на чтение данных менеджера
                  MANAGER_UPDATE, // Право на обновление данных менеджера
                  MANAGER_DELETE, // Право на удаление данных менеджера
                  MANAGER_CREATE // Право на создание данных менеджера
          )
  ),
  MANAGER( // Роль менеджера с ограниченным набором прав
          Set.of(
                  MANAGER_READ, // Право на чтение данных менеджера
                  MANAGER_UPDATE, // Право на обновление данных менеджера
                  MANAGER_DELETE, // Право на удаление данных менеджера
                  MANAGER_CREATE // Право на создание данных менеджера
          )
  );

  @Getter
  private final Set<Permission> permissions; // Поле для хранения набора прав (permissions) для каждой роли

  // Метод для получения списка SimpleGrantedAuthority для роли
  public List<SimpleGrantedAuthority> getAuthorities() {
    // Преобразуем каждое право (permission) в SimpleGrantedAuthority и собираем в список
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    // Добавляем роль как SimpleGrantedAuthority
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities; // Возвращаем список SimpleGrantedAuthority
  }
}
