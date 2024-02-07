package com.antonio.hundirlaflota.Servicios;

import com.antonio.hundirlaflota.Modelos.Usuario;

import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final HttpServletRequest request;
    private final JwtTokenProvider jwtTokenProvider;


    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(Usuario usuario) {
        String token = jwtTokenProvider.generateToken(usuario.getEmail(), JwtTokenProvider.getMIDDLEEXPIRATIONTIME());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(usuario.getEmail());
        message.setSubject("Email Confirmation");
        String baseUrl = getBaseUrl();
        message.setText("Click the following link to confirm your email: "
                + baseUrl + "/confirmar?token=" + token);
        javaMailSender.send(message);
    }

    private String getBaseUrl() {

        String hostName = request.getServerName();
        int serverPort = request.getServerPort();
        return "http://" + hostName + ("localhost".equals(hostName) ? ":" + serverPort : "");
    }
}