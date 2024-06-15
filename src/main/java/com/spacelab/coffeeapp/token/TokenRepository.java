package com.spacelab.coffeeapp.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> { // Интерфейс для работы с токенами в базе данных, расширяющий JpaRepository

  // Запрос для поиска всех действующих (не истёкших и не отозванных) токенов пользователя по его ID
  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Long id);

  // Метод для поиска токена по его значению
  Optional<Token> findByToken(String token);
}
