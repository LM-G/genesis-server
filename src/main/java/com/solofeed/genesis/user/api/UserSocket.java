package com.solofeed.genesis.user.api;

import com.solofeed.genesis.user.api.dto.PongDto;
import com.solofeed.genesis.user.domain.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Socket handler for {@link User} related stuff
 */
@Controller
public class UserSocket {

    @MessageMapping("/ping")
    @SendTo("/queue/pingpong")
    public PongDto pong() {
        return new PongDto();
    }
}
