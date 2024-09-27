package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.entity.PasswordResetToken;
import com.spacelab.coffeeapp.entity.User;
import com.spacelab.coffeeapp.repository.PasswordResetTokenRepository;
import com.spacelab.coffeeapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PasswordResetTokenServiceImplTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    private User user;
    private PasswordResetToken passwordResetToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken("valid-token");
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(LocalDateTime.now().plusHours(1));
    }

    @Test
    void testUpdatePassword() {
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.of(passwordResetToken));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        passwordResetTokenService.updatePassword("valid-token", "newPassword");

        verify(passwordEncoder, times(1)).encode("newPassword");
        assertEquals("encodedPassword", user.getPassword());
        verify(passwordResetTokenRepository, times(1)).save(passwordResetToken);
    }

    @Test
    void testUpdatePassword_TokenNotFound() {
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> passwordResetTokenService.updatePassword("invalid-token", "newPassword"));
    }

    @Test
    void testValidatePasswordResetToken_Valid() {
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.of(passwordResetToken));

        boolean isValid = passwordResetTokenService.validatePasswordResetToken("valid-token");

        assertTrue(isValid);
    }

    @Test
    void testValidatePasswordResetToken_Expired() {
        passwordResetToken.setExpirationDate(LocalDateTime.now().minusHours(1));
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.of(passwordResetToken));

        boolean isValid = passwordResetTokenService.validatePasswordResetToken("expired-token");

        assertFalse(isValid);
    }

    @Test
    void testValidatePasswordResetToken_TokenNotFound() {
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        boolean isValid = passwordResetTokenService.validatePasswordResetToken("invalid-token");

        assertFalse(isValid);
    }

    @Test
    void testCreateAndSavePasswordResetToken_NewToken() {
        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        user.setPasswordResetToken(null);
        String token = passwordResetTokenService.createAndSavePasswordResetToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        verify(passwordResetTokenRepository, times(1)).save(tokenCaptor.capture());
        assertEquals(token, tokenCaptor.getValue().getToken());
    }



    @Test
    void testCreateAndSavePasswordResetToken_UpdateExistingToken() {
        PasswordResetToken existingToken = new PasswordResetToken();
        existingToken.setToken("old-token");
        user.setPasswordResetToken(existingToken);

        when(userRepository.save(any(User.class))).thenReturn(user);

        String token = passwordResetTokenService.createAndSavePasswordResetToken(user);

        assertNotNull(token);
        assertEquals(token, existingToken.getToken());
        verify(userRepository, times(1)).save(user);
    }
}
