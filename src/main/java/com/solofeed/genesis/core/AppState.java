package com.solofeed.genesis.core;

import com.solofeed.genesis.user.api.dto.UserDto;
import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Application state
 */
public class AppState {
    /** Users who are connected */
    @Getter
    List<UserDto> connectedUsers;

    public AppState() {
        this.connectedUsers = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Add a user to currently connected user list
     * @param user user to add to the list
     */
    @Async
    public void addConnectedUser(UserDto user){
        connectedUsers.add(user);
    }

    /**
     * Remove a user from currently connected user list
     * @param user user to remove from the list
     */
    @Async
    public void removeConnectedUser(UserDto user){
        connectedUsers.removeIf(connectedUser -> connectedUser.getId().equals(user.getId()));
    }
}
