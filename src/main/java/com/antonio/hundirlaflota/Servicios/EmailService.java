package com.antonio.hundirlaflota.Servicios;

import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.EmailData;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @SneakyThrows
    public void sendEmail(EmailData emailData) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(emailData.getEmail());
        helper.setSubject("Email Confirmation");
        helper.setText("<p>Click the following <a href='" + getBaseUrl() + emailData.getPath() + "?token=" + emailData.getToken() + "'>link</a>" + emailData.getMessage() + "</p>", true);
        javaMailSender.send(message);
    }

    private String getBaseUrl() {
        String scheme = request.getScheme() + "://";
        String hostName = request.getServerName();
        int serverPort = request.getServerPort();
        return scheme + hostName + ("localhost".equals(hostName) ? ":" + serverPort : "/api");
    }
}