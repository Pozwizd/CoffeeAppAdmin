package com.spacelab.coffeeapp.config;


import com.spacelab.coffeeapp.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter  {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (request.getServletPath().contains("/api/v1/auth") ||
            request.getServletPath().startsWith("/css/") ||
            request.getServletPath().startsWith("/js/") ||
            request.getServletPath().startsWith("/assets/**") ||
            request.getServletPath().startsWith("/assets/")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt;
    final String userEmail;

    jwt = extractJwtAccessTokenFromCookie(request);

    if (jwt == null) {
      filterChain.doFilter(request, response);
      return;
    }
    userEmail = jwtService.extractUsername(jwt);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // Загружаем детали пользователя
      var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked()) // Проверяем, что токен не истёк и не отозван
              .orElse(false);

      // Если токен действителен и пользователь из токена совпадает с загруженным пользователем
      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities() // Роли и права пользователя
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request) // Устанавливаем детали аутентификации
        );
        SecurityContextHolder.getContext().setAuthentication(authToken); // Устанавливаем аутентификацию в контексте безопасности
      }
    }
    // Продолжаем обработку запроса
    filterChain.doFilter(request, response);
  }

  private String extractJwtAccessTokenFromCookie(HttpServletRequest request) {
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
  private String extractJwtRefreshTokenFromCookie(HttpServletRequest request) {
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
