package com.spacelab.coffeeapp.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@jakarta.persistence.Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Entity2 {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 4, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    private String field1;
    @Size(min = 4, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    private String field2;
    @Size(min = 4, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    private String field3;

}
