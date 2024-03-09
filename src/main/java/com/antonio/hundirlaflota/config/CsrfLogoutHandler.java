package com.antonio.hundirlaflota.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CsrfLogoutHandler implements LogoutSuccessHandler {

    private final CsrfTokenRepository csrfTokenRepository;

    private final LogoutSuccessHandler delegate = new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK);
    @Value("${frontend.url}")
    private String frontEndUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CsrfToken csrfToken = this.csrfTokenRepository.generateToken(request);
        this.csrfTokenRepository.saveToken(csrfToken, request, response);
        this.delegate.onLogoutSuccess(request, response, authentication);
        response.sendRedirect(frontEndUrl);
    }

}

