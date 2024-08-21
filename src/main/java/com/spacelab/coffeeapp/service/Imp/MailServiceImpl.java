package com.spacelab.coffeeapp.service.Imp;

import com.spacelab.coffeeapp.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;



    public MailServiceImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendToken(String token, String to, HttpServletRequest httpRequest) {
        log.info("sendToken() - Sending token to " + to);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("sender@example.com");
            helper.setTo(to);
            helper.setSubject("Reset password");
            helper.setText(build(token, httpRequest), true);

            mailSender.send(message);
            log.info("sendToken() - Token was sent");

        } catch (MessagingException ex) {
            log.error(ex.getMessage());
        }
    }

    private String build(String token, HttpServletRequest httpRequest) {
        Context context = new Context();
        final String fullUrl = ServletUriComponentsBuilder.fromRequestUri(httpRequest).build().toUriString();
        log.info("url: " + fullUrl);
        int in = fullUrl.lastIndexOf("/");
        String baseUrl = fullUrl.substring(0, in);
        String l = baseUrl + "/changePassword?token=" + token;
        context.setVariable("link", l);
        return templateEngine.process("email/emailTemplate", context);
    }
}
