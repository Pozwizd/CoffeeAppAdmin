package com.spacelab.coffeeapp.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacelab.coffeeapp.config.JwtService;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.repository.UserRepository;
import com.spacelab.coffeeapp.token.Token;
import com.spacelab.coffeeapp.token.TokenRepository;
import com.spacelab.coffeeapp.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service // Эта аннотация говорит Spring, что этот класс является сервисом и должен быть зарегистрирован в контейнере Spring
@RequiredArgsConstructor // Эта аннотация автоматически создаёт конструктор с аргументами для всех финальных полей
public class AuthenticationService {

  // Поля для работы с репозиториями, шифрованием паролей, JWT и аутентификацией
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  // Метод для регистрации нового пользователя
  public AuthenticationResponse register(RegisterRequest request) {
    // Создаём нового пользователя на основе данных из запроса
    var user = User.builder()
            .name(request.getFirstname()) // Устанавливаем имя пользователя
            .email(request.getEmail()) // Устанавливаем email пользователя
            .password(passwordEncoder.encode(request.getPassword())) // Шифруем и устанавливаем пароль пользователя
            .role(request.getRole()) // Устанавливаем роль пользователя
            .build();

    var savedUser = repository.save(user); // Сохраняем пользователя в базе данных
    var jwtToken = jwtService.generateToken(user); // Генерируем JWT токен для пользователя
    var refreshToken = jwtService.generateRefreshToken(user); // Генерируем refresh токен для пользователя
    saveUserToken(savedUser, jwtToken); // Сохраняем токен пользователя в базе данных

    // Возвращаем ответ с токенами
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  // Метод для аутентификации пользователя
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    // Проводим аутентификацию пользователя с использованием его email и пароля
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    var user = repository.findByEmail(request.getEmail()) // Ищем пользователя по email
            .orElseThrow(); // Если пользователь не найден, выбрасываем исключение

    var jwtToken = jwtService.generateToken(user); // Генерируем JWT токен для пользователя
    var refreshToken = jwtService.generateRefreshToken(user); // Генерируем refresh токен для пользователя
    revokeAllUserTokens(user); // Отзываем все существующие токены пользователя
    saveUserToken(user, jwtToken); // Сохраняем новый токен пользователя в базе данных

    // Возвращаем ответ с токенами
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  // Метод для сохранения токена пользователя в базе данных
  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user) // Устанавливаем пользователя для токена
            .token(jwtToken) // Устанавливаем значение токена
            .tokenType(TokenType.BEARER) // Устанавливаем тип токена (Bearer)
            .expired(false) // Устанавливаем, что токен не истёк
            .revoked(false) // Устанавливаем, что токен не отозван
            .build();

    tokenRepository.save(token); // Сохраняем токен в базе данных
  }

  // Метод для отзыва всех существующих токенов пользователя
  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId()); // Ищем все действующие токены пользователя
    if (validUserTokens.isEmpty()) // Если нет действующих токенов, выходим из метода
      return;

    validUserTokens.forEach(token -> {
      token.setExpired(true); // Помечаем токен как истёкший
      token.setRevoked(true); // Помечаем токен как отозванный
    });

    tokenRepository.saveAll(validUserTokens); // Сохраняем все обновлённые токены в базе данных
  }

  // Метод для обновления токена
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Извлекаем заголовок авторизации
    final String refreshToken;
    final String userEmail;

    // Если заголовок авторизации пустой или не начинается с "Bearer ", выходим из метода
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }

    refreshToken = authHeader.substring(7); // Извлекаем refresh токен из заголовка (убираем "Bearer ")
    userEmail = jwtService.extractUsername(refreshToken); // Извлекаем email пользователя из токена

    if (userEmail != null) { // Если email пользователя не пустой
      var user = this.repository.findByEmail(userEmail) // Ищем пользователя по email
              .orElseThrow(); // Если пользователь не найден, выбрасываем исключение

      if (jwtService.isTokenValid(refreshToken, user)) { // Проверяем, действителен ли токен
        var accessToken = jwtService.generateToken(user); // Генерируем новый access токен для пользователя
        revokeAllUserTokens(user); // Отзываем все существующие токены пользователя
        saveUserToken(user, accessToken); // Сохраняем новый токен пользователя в базе данных

        // Формируем ответ с новыми токенами
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse); // Записываем ответ в поток вывода
      }
    }
  }
}
