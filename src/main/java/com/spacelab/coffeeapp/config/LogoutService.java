package com.spacelab.coffeeapp.config;


import com.spacelab.coffeeapp.token.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service // Эта аннотация говорит Spring, что этот класс является сервисом и должен быть зарегистрирован в контейнере Spring
@RequiredArgsConstructor // Эта аннотация автоматически создаёт конструктор с аргументами для всех финальных полей
public class LogoutService implements LogoutHandler { // Этот класс реализует интерфейс LogoutHandler для обработки выхода из системы

  private final TokenRepository tokenRepository; // Репозиторий для работы с токенами

  @Override
  public void logout(
          HttpServletRequest request, // Запрос, который пришёл на сервер
          HttpServletResponse response, // Ответ, который сервер отправит клиенту
          Authentication authentication // Информация об аутентификации пользователя
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    jwt = extractJwtFromCookie(request);

    if (authHeader == null) {
      return;
    }


    var storedToken = tokenRepository.findByToken(jwt) // Ищем токен в репозитории
            .orElse(null); // Если токен не найден, возвращаем null

    // Если токен найден в репозитории
    if (storedToken != null) {
      storedToken.setExpired(true); // Помечаем токен как истёкший
      storedToken.setRevoked(true); // Помечаем токен как отозванный
      tokenRepository.save(storedToken); // Сохраняем обновлённый токен в репозитории
      SecurityContextHolder.clearContext(); // Очищаем контекст безопасности
    }
  }

  private String extractJwtFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("accessToken")) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
