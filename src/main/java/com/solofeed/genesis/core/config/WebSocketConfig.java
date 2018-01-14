package com.solofeed.genesis.core.config;

import com.solofeed.genesis.core.converter.MappingGsonMessageConverter;
import com.solofeed.genesis.core.security.api.interceptor.AuthenticationHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.List;

/**
 * Configurates the STOMP websocket handler
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    private final MappingGsonMessageConverter mappingGsonMessageConverter;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api-ws/v1/")
                .setAllowedOrigins("*")
                .addInterceptors(authenticationHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue/");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(mappingGsonMessageConverter);
        return true;
    }

    @Bean
    public AuthenticationHandshakeInterceptor authenticationHandshakeInterceptor() {
        return new AuthenticationHandshakeInterceptor();
    }
}
