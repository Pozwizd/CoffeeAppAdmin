package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.auth.AuthenticationRequest;
import com.spacelab.coffeeapp.auth.AuthenticationResponse;
import com.spacelab.coffeeapp.auth.AuthenticationService;
import com.spacelab.coffeeapp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService service;



    @GetMapping("/login")
    public ModelAndView login(Model model){
        model.addAttribute("title", "Вход в систему");

        return new ModelAndView("/login");
    }




//    @PostMapping("/login")
//    public void authenticateUser(@RequestParam(name = "email") String email,
//                                 @RequestParam(name = "password") String password,
//                                 HttpServletRequest request) {
//        Authentication authentication;
//        try {authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
//            SecurityContext context = SecurityContextHolder.getContext();
//            context.setAuthentication(authentication);
//            HttpSession session = request.getSession(true);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
//        } catch (BadCredentialsException e) {
//            new ModelAndView("redirect:/login", HttpStatus.BAD_REQUEST);
//        }
//    }


    @PostMapping("/login")
    public void authenticateUser(@RequestParam(name = "email") String email,
                                 @RequestParam(name = "password") String password,
                                 HttpServletRequest request) {
        Authentication authentication;
        try {authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);
        } catch (BadCredentialsException e) {
            new ModelAndView("redirect:/login", HttpStatus.BAD_REQUEST);
        }
    }





    @GetMapping("/logout")
    public ModelAndView performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return new ModelAndView("redirect:/");
    }

}
