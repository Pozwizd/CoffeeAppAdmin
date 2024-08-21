package com.spacelab.coffeeapp.service;

import com.spacelab.coffeeapp.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface PasswordResetTokenService {
    void updatePassword(String token,String password);
    boolean validatePasswordResetToken(String token);
    String createAndSavePasswordResetToken(User user);
}
