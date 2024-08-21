package com.spacelab.coffeeapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendToken(String token, String to, HttpServletRequest request);
}
