package com.spacelab.coffeeapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Эта аннотация говорит Spring, что этот класс является сервисом, который можно использовать в приложении
public class JwtService {

  @Value("${application.security.jwt.secret-key}")
  private String secretKey; // Этот параметр хранит секретный ключ для подписывания JWT, он берётся из настроек приложения
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration; // Этот параметр хранит время жизни JWT (сколько времени JWT будет действителен)
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration; // Этот параметр хранит время жизни токена обновления

  // Этот метод извлекает имя пользователя из токена
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject); // Он использует метод extractClaim для получения имени пользователя
  }

  // Этот метод извлекает конкретное требование (claim) из токена
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token); // Сначала извлекаются все требования
    return claimsResolver.apply(claims); // Затем возвращается конкретное требование, определённое в claimsResolver
  }

  // Этот метод создаёт JWT для пользователя без дополнительных требований
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails); // Он вызывает другой метод generateToken, передавая пустую карту дополнительных требований
  }

  // Этот метод создаёт JWT для пользователя с дополнительными требованиями
  public String generateToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration); // Он вызывает метод buildToken, передавая дополнительные требования и время жизни токена
  }

  // Этот метод создаёт токен обновления для пользователя
  public String generateRefreshToken(
          UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration); // Он вызывает метод buildToken с временем жизни токена обновления
  }

  // Этот метод строит JWT с заданными требованиями, именем пользователя и временем жизни
  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    return Jwts
            .builder() // Создаёт строитель JWT
            .setClaims(extraClaims) // Устанавливает дополнительные требования
            .setSubject(userDetails.getUsername()) // Устанавливает имя пользователя как субъект
            .setIssuedAt(new Date(System.currentTimeMillis())) // Устанавливает текущую дату как дату выдачи
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Устанавливает время истечения
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Подписывает токен с использованием секретного ключа и алгоритма HS256
            .compact(); // Строит и возвращает JWT
  }

  // Этот метод проверяет, действителен ли токен для данного пользователя
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token); // Сначала извлекается имя пользователя из токена
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // Проверяет, совпадает ли имя пользователя и не истёк ли токен
  }

  // Этот метод проверяет, истёк ли срок действия токена
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date()); // Сравнивает дату истечения токена с текущей датой
  }

  // Этот метод извлекает дату истечения токена
  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration); // Использует метод extractClaim для получения даты истечения
  }

  // Этот метод извлекает все требования из токена
  private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder() // Создаёт строитель парсера JWT
            .setSigningKey(getSignInKey()) // Устанавливает ключ для проверки подписи
            .build() // Строит парсер
            .parseClaimsJws(token) // Парсит токен
            .getBody(); // Возвращает требования
  }

  // Этот метод возвращает ключ для подписи токена
  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Декодирует секретный ключ из строки Base64
    return Keys.hmacShaKeyFor(keyBytes); // Возвращает ключ для алгоритма HMAC SHA
  }
}

