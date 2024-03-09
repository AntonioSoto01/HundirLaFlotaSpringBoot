package com.antonio.hundirlaflota.config;


import com.antonio.hundirlaflota.config.jwt.JwtAuthenticationFilter;
import com.antonio.hundirlaflota.config.jwt.JwtTokenProvider;
import com.antonio.hundirlaflota.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.antonio.hundirlaflota.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.antonio.hundirlaflota.config.oauth2.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Value("${frontend.url}")
    private String frontendUrl;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSucessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final CsrfTokenRepository csrfTokenRepository;
    private final CsrfLogoutHandler csrfLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/**", "/home", "/login", "/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customizer -> customizer.
                        sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .exceptionHandling(exceptionHandling -> exceptionHandling.
                        authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(csrfLogoutHandler))
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                // .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(frontendUrl + "/").permitAll())
                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(config -> config
//                                .authorizationRequestRepository(cookieAuthorizationRequestRepository))
                                .successHandler(oAuth2AuthenticationSucessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)

                );
        return http.build();
    }


    final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
        private final XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {

            // set the name of the attribute the CsrfToken will be populated on
            delegate.setCsrfRequestAttributeName(null);
            /*
             * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
             * the CsrfToken when it is rendered in the response body.
             */
            this.delegate.handle(request, response, csrfToken);
        }

        @Override
        public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
            /*
             * If the request contains a request header, use CsrfTokenRequestAttributeHandler
             * to resolve the CsrfToken. This applies when a single-page application includes
             * the header value automatically, which was obtained via a cookie containing the
             * raw CsrfToken.
             */
            if (StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()))) {
                return super.resolveCsrfTokenValue(request, csrfToken);
            }
            /*
             * In all other cases (e.g. if the request contains a request parameter), use
             * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
             * when a server-side rendered form includes the _csrf request parameter as a
             * hidden input.
             */

            return this.delegate.resolveCsrfTokenValue(request, csrfToken);
        }
    }


}
