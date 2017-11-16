package com.solofeed.genesis.echo.api;

import com.solofeed.genesis.echo.service.CounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Log4j2
@RequiredArgsConstructor
@Controller
public class EchoController {
    private final CounterService counterService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/channel/public")
    public String sendMessage(@Payload String chatMessage) {
        return chatMessage;
    }
}
