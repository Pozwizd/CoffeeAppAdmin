package com.spacelab.coffeeapp.controller;


import com.spacelab.coffeeapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice(basePackages = "com.spacelab.coffeeapp.controller")
@AllArgsConstructor
public class UserControllerAdvice {

    private final UserRepository userRepository;


    @ModelAttribute
    public void addCommonAttributes(Model model, Principal principal) {



        if (principal != null) {
            model.addAttribute("userName", userRepository
                    .findUserByEmail(principal.getName()).get().getName());
            model.addAttribute("userRole", userRepository
                    .findUserByEmail(principal.getName()).get().getRole());
            String email = principal.getName();
            System.out.println(email);
        } else {
            System.out.println("No principal");
        }
    }
}
