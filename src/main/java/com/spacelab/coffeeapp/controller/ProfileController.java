package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.mapper.UserMapper;
import com.spacelab.coffeeapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserMapper userMapper;
    private final UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("title", "Редактирование профиля");
    }


    @GetMapping("/")
    public ModelAndView getUserProfile() {


        return new ModelAndView("user/profile");
    }

    @GetMapping("/profile")
    @ResponseBody
    public UserDto getCurrentUser(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userMapper.toDto((User) auth.getPrincipal());
    }

    @PutMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> updateEntity(@Valid @RequestBody UserDto user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        user.setId(((User) auth.getPrincipal()).getId());
        userService.updateUser(user.getId() , user);
        return ResponseEntity.ok().build();
    }




}
