package com.spacelab.coffeeapp.controller;


import com.spacelab.coffeeapp.dto.UserDto;
import com.spacelab.coffeeapp.entity.Role;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.mapper.UserMapper;
import com.spacelab.coffeeapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RequestMapping("/user")
@AllArgsConstructor
@Controller
@Slf4j
public class UserController {

    private final UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "users");
        model.addAttribute("title", "Пользователи");
    }

    @GetMapping("/roles")
    public @ResponseBody List<Role> getRole() {
        return List.of(Role.ADMIN, Role.MANAGER);
    }


    @GetMapping({"/", ""})
    public ModelAndView getUserPage( HttpSession session) {
        User user = new User();
        user.setName("test");
        user.setPassword("test");
        session.setAttribute("userList", user);
        return new ModelAndView("user/usersPage");
    }

    @GetMapping("/getAll")
    @ResponseBody
    public Page<UserDto> getEntities(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "") String search,
                                     @RequestParam(defaultValue = "5") Integer size,
                                     HttpSession session) {

        return userService.getUsersDtosByRequest(page, size, search);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getEntity(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping({"/create"})
    @ResponseBody
    public ResponseEntity<?> createEntity(@RequestBody UserDto user) {
         userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> updateEntity(@PathVariable Long id, @Valid @RequestBody UserDto user) {

        userService.updateUser(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteEntity(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
