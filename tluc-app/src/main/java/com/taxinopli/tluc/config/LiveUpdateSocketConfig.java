package com.taxinopli.tluc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class LiveUpdateSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final String WS_RECEPTION = "/ws";
    private static final String LIVE_UPDATE_ENDPOINT = "/live";

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint(WS_RECEPTION)
                .setAllowedOriginPatterns("*") // Allow all origins for development
                .withSockJS(); // Enable SockJS fallback
    }

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes(LIVE_UPDATE_ENDPOINT);
        config.enableSimpleBroker("/topic"); // Enable simple broker for /topic destination
    }
}
