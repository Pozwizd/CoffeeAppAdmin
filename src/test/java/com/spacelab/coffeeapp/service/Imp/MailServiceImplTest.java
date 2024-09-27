package com.spacelab.coffeeapp.service.Imp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private MailServiceImpl mailService;

    private MockHttpServletRequest httpRequest;

    @BeforeEach
    public void setup() {
        httpRequest = new MockHttpServletRequest();
        httpRequest.setRequestURI("/api/reset");
    }

    @Test
    public void testSendToken_Success() throws MessagingException {
        String token = "test-token";
        String recipientEmail = "recipient@example.com";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String expectedTemplateOutput = "<html><body>Click <a href='http://localhost/changePassword?token=test-token'>here</a> to reset your password</body></html>";
        when(templateEngine.process(eq("email/emailTemplate"), any(Context.class))).thenReturn(expectedTemplateOutput);

        mailService.sendToken(token, recipientEmail, httpRequest);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(eq("email/emailTemplate"), any(Context.class));
    }

    @Test
    public void testSendToken_MessagingException() throws MessagingException {
        String token = "test-token";
        String recipientEmail = "recipient@example.com";

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Добавляем мокающий вывод шаблона, чтобы избежать передачи null в setText
        when(templateEngine.process(eq("email/emailTemplate"), any(Context.class)))
                .thenReturn("<html><body>Sample Template</body></html>");

        // Симулируем ошибку отправки сообщения
        doAnswer((Answer<Void>) invocation -> {
            throw new MessagingException("Test exception");
        }).when(mailSender).send(any(MimeMessage.class));

        // Вызов метода
        mailService.sendToken(token, recipientEmail, httpRequest);

        // Проверяем создание сообщения и попытку отправки
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);

        // Проверяем, что шаблон все равно создается
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }
}