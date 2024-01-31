package com.antonio.hundirlaflota;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {


    @Bean
    WebClient webClient(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        var oauth = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository));
        return WebClient.builder()
                .apply(oauth.oauth2Configuration())
                .build();
    }


}
