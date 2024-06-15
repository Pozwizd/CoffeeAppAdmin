package com.spacelab.coffeeapp.auditing;


import com.spacelab.coffeeapp.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Long> { // Этот класс реализует интерфейс AuditorAware для получения текущего аудитора

    @Override
    public Optional<Long> getCurrentAuditor() {
        // Получаем текущую аутентификацию (пользователя) из контекста безопасности
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // Если аутентификация равна null, не аутентифицирована или анонимна, возвращаем пустой Optional
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Извлекаем текущего аутентифицированного пользователя из аутентификации
        User userPrincipal = (User) authentication.getPrincipal();
        // Возвращаем ID пользователя как текущего аудитора
        return Optional.ofNullable(userPrincipal.getId());
    }
}
