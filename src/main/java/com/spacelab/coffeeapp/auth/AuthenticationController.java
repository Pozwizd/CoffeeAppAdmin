package com.spacelab.coffeeapp.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
          @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  // Эндпоинт для аутентификации пользователя
  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(
          @RequestBody AuthenticationRequest request, // Тело запроса содержит данные для аутентификации
          HttpServletResponse response
  ) {
    AuthenticationResponse authResponse = service.authenticate(request);

    // Сохранение access_token в куки
    Cookie accessCookie = new Cookie("accessToken", authResponse.getAccessToken());
    accessCookie.setPath("/"); // Установите путь, если требуется
    accessCookie.setHttpOnly(true); // Важно: делайте куки HTTP-only!
    response.addCookie(accessCookie);

    // Сохранение refresh_token в куки
    Cookie refreshCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
    refreshCookie.setPath("/"); // Установите путь, если требуется
    refreshCookie.setHttpOnly(true); // Важно: делайте куки HTTP-only!
    response.addCookie(refreshCookie);

    return ResponseEntity.ok(authResponse);
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}
