package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.PasswordResetToken;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.repository.PasswordResetTokenRepository;
import com.spacelab.coffeeapp.repository.UserRepository;
import com.spacelab.coffeeapp.service.PasswordResetTokenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(String token, String password) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(()-> new EntityNotFoundException("Password reset token was not found by token "+token));
        passwordResetToken.getUser().setPassword(passwordEncoder.encode(password));
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);
        boolean isValid = passwordResetToken.isPresent() && !passwordResetToken.get().getExpirationDate().isBefore(LocalDateTime.now());
        return isValid;
    }


    @Override
    public String createAndSavePasswordResetToken(User admin) {
        String token = UUID.randomUUID().toString();
        if(admin.getPasswordResetToken() != null){
            admin.getPasswordResetToken().setToken(token);
            admin.getPasswordResetToken().setExpirationDate();
            userRepository.save(admin);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, admin);
            passwordResetTokenRepository.save(passwordResetToken);
        }
        return token;
    }
}
