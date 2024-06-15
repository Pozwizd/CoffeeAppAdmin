package com.spacelab.coffeeapp.config;


import com.spacelab.coffeeapp.auditing.ApplicationAuditAware;
import com.spacelab.coffeeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  private final UserRepository repository;

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Bean // Ещё один Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Создаём объект, который будет проверять подлинность пользователя
    authProvider.setUserDetailsService(userDetailsService()); // Говорим ему использовать наш метод для поиска пользователей
    authProvider.setPasswordEncoder(passwordEncoder()); // Говорим ему использовать наш метод для шифрования паролей
    return authProvider; // Возвращаем настроенный объект
  }

  @Bean // Ещё один Bean
  public AuditorAware<Long> auditorAware() {
    return new ApplicationAuditAware(); // Возвращаем объект, который поможет отслеживать изменения в приложении
  }

  @Bean // Ещё один Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager(); // Возвращаем менеджер аутентификации, который помогает управлять процессом входа в систему
  }

  @Bean // Ещё один Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance(); // Возвращаем объект, который шифрует пароли
  }
}
