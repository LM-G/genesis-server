package com.solofeed.genesis.echo.api;

import com.solofeed.genesis.echo.service.EchoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.ws.rs.ext.Provider;

@Log4j2
@Component
@RequiredArgsConstructor
public class EchoSocketHandler extends AbstractWebSocketHandler {
    private final EchoService echoService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.debug("Opened new session in instance " + this);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String echoMessage = echoService.getMessage(message.getPayload());
        LOGGER.debug("Message echo: ", echoMessage);
        session.sendMessage(new TextMessage(echoMessage));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.debug("Erreur: ", session, exception);
        session.close(CloseStatus.SERVER_ERROR);
    }
}
