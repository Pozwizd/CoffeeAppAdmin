package com.spacelab.coffeeapp;


import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class CoffeeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeAppApplication.class, args);



    }

    @Bean
    public Faker faker() {
        return new Faker();
    }
}
