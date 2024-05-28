package com.spacelab.coffeeapp.controller.admin;

import com.spacelab.coffeeapp.entity.Role;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private AuthenticationManager authenticationManager;
    private final int pageSize = 5;

    @GetMapping({"/", ""})
    public ModelAndView viewUsers(Model model) {
        Page<User> users = userService.findAllUsers(1, pageSize);
        model.addAttribute("title", "Пользователи");
        model.addAttribute("pageActive", "users");
        model.addAttribute("users", users);
        return new ModelAndView("user/usersPage");
    }

    @GetMapping("/roles")
    @ResponseBody
    public List<Role> getRole() {
        return List.of(Role.ADMIN, Role.MANAGER);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        userService.saveUser(userDetails);

        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody User userDetails) {
        userService.saveUser(userDetails);
        return ResponseEntity.ok(userDetails);
    }



    @GetMapping("/getPage")
    public @ResponseBody Page<User> getUsersOnPage(@RequestParam int page) {
        return userService.findAllUsers(page, pageSize);
    }

    @GetMapping("/getPageSearch")
    public @ResponseBody Page<User> getUsersPageBySearch(@RequestParam int page, @RequestParam String search) {
        return userService.findUsersByRequest(page, pageSize, search);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(userService.getUser(id));
        return ResponseEntity.ok().build();
    }
}
