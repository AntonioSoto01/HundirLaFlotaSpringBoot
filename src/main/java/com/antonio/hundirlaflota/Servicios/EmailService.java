package com.antonio.hundirlaflota.Servicios;

import com.antonio.hundirlaflota.Modelos.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String token, Usuario usuario) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(usuario.getEmail());
        message.setSubject("Email Confirmation");
        message.setText("Click the following link to confirm your email: "
                + frontendUrl + "/confirmar?token=" + token);
        javaMailSender.send(message);
    }
}
