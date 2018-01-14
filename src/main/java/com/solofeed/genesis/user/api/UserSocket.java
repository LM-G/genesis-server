package com.solofeed.genesis.user.api;

import com.solofeed.genesis.user.api.dto.PongDto;
import com.solofeed.genesis.user.domain.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Socket handler for {@link User} related stuff
 */
@Log4j2
@Controller
public class UserSocket {

    @MessageMapping("/ping")
    @SendTo("/queue/pong")
    public PongDto ping(String message) {
        LOGGER.debug("From \"/ping\" to \"/pong\" : " + message);
        return new PongDto();
    }
}
