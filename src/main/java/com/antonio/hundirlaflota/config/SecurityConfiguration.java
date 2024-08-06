package com.antonio.hundirlaflota.config;

import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.antonio.hundirlaflota.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.antonio.hundirlaflota.config.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.session.jdbc.MySqlJdbcIndexedSessionRepositoryCustomizer;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSucessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/iniciar-juego-2-jugadores", "/email-recibido").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .logout(logout -> logout
                        .logoutSuccessUrl(frontendUrl + "/").permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        // .authorizationEndpoint(config -> config
                        // .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                        // )
                        .successHandler(oAuth2AuthenticationSucessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                )
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeServices(new SpringSessionRememberMeServices())
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/ws/**")
                )
                // .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),BasicAuthenticationFilter.class)
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);

        return http.build();
    }

    //https://stackoverflow.com/questions/50696183/spring-jdbc-session-causes-duplicate-entry-exception
    @Bean
    public MySqlJdbcIndexedSessionRepositoryCustomizer sessionRepositoryCustomizer() {
        return new MySqlJdbcIndexedSessionRepositoryCustomizer();
    }


}
