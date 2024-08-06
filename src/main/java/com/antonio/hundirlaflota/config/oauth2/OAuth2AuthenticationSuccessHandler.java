package com.antonio.hundirlaflota.config.oauth2;

import com.antonio.hundirlaflota.Modelos.Usuario;
import com.antonio.hundirlaflota.Repositorios.UsuarioRepository;
import com.antonio.hundirlaflota.Servicios.UsuarioService;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.dto.GitHubEmail;
import com.antonio.hundirlaflota.mappers.JuegoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final WebClient webClient;
    @Value("${frontend.url}")
    private String frontendUrl;
    private final JuegoMapper juegoMapper;
    private final OAuth2AuthenticationFailureHandler failureHandler;
    private final UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String providerName = oauthToken.getAuthorizedClientRegistrationId();
        String email = oauthToken.getPrincipal().getAttribute("email");
        String name = oauthToken.getPrincipal().getAttribute("name");
        // If email is null and provider is GitHub, try to find GitHub email
        if (email == null && "github".equals(providerName)) {
            email = findGithubEmail();
        }

        handleUserAuthentication(email, name, providerName, response, request);
    }

    private void handleUserAuthentication(String email, String name, String providerName,
                                          HttpServletResponse response, HttpServletRequest request) throws IOException {
        Optional<Usuario> existingUser = usuarioRepository.findByEmail(email);
        Usuario usuario;
        if (existingUser.isPresent()) {
            usuario = existingUser.get();
            if (!usuario.getProveedor().equals(providerName)) {
                SecurityContextHolder.clearContext();
                failureHandler.onAuthenticationFailure(request, response, new AuthenticationException("This email is associated with " + usuario.getProveedor()) {
                });
                return;
            } else {
                // Handle scenario where user is already registered with the same provider
            }
        } else {
            // User does not exist, create and save a new user
            usuario = new Usuario();
            usuario.setNombre(name);
            usuario.setEmail(email);
            usuario.setProveedor(providerName);
            usuarioRepository.save(usuario);
            System.out.println("Usuario creado: " + usuario);
        }
        usuarioService.saveContext(usuario, request, response);
        response.sendRedirect(frontendUrl);
    }

    private String findGithubEmail() {
        // GitHub API call to retrieve user emails
        List<GitHubEmail> githubEmails = webClient.get()
                .uri("https://api.github.com/user/emails")
                .attributes(clientRegistrationId("github"))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GitHubEmail>>() {
                })
                .block();

        if (githubEmails != null) {
            return githubEmails.stream()
                    .filter(GitHubEmail::isPrimary)
                    .map(GitHubEmail::getEmail)
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }


}