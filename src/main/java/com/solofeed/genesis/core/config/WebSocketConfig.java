package com.solofeed.genesis.core.config;

import com.solofeed.genesis.echo.api.EchoSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@Controller
public class WebSocketConfig implements WebSocketConfigurer {
    private final EchoSocketHandler echoSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoSocketHandler, "/echo").setAllowedOrigins("*");
    }
}
