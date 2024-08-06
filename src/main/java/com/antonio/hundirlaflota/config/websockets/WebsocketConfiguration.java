package com.antonio.hundirlaflota.config.websockets;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebsocketConfiguration extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {
    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${spring.rabbitmq.stomp.username}")
    private String springStompUsername;

    @Value("${spring.rabbitmq.stomp.password}")
    private String springStompPassword;

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins(frontendUrl)
                .withSockJS();
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins(frontendUrl);

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
//        registry.enableSimpleBroker("/topic");
        registry.enableStompBrokerRelay("/topic")
                .setUserDestinationBroadcast("/topic/unresolved.user.dest")
                .setUserRegistryBroadcast("/topic/registry.broadcast")

//                .setClientLogin(springStompUsername)
//                .setClientPasscode(springStompPassword)
//                .setSystemLogin(springStompUsername)
//                .setSystemPasscode(springStompPassword)
        ;
    }


}