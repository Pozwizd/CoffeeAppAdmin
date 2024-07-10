package com.spacelab.coffeeapp.config;

import com.spacelab.coffeeapp.auth.AuthenticationRequest;
import com.spacelab.coffeeapp.auth.AuthenticationResponse;
import com.spacelab.coffeeapp.auth.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;

    public CustomAuthenticationSuccessHandler(@Lazy AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        // Дополнительная логика получения пароля (если необходимо)
        AuthenticationRequest authRequest = new AuthenticationRequest(username, "password"); // Замените на правильный способ получения пароля
        AuthenticationResponse authResponse = authenticationService.authenticate(authRequest);

        Cookie accessCookie = new Cookie("accessToken", authResponse.getAccessToken());
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);

        response.sendRedirect("/");
    }
}
