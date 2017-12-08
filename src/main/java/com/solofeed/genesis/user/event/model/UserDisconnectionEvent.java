package com.solofeed.genesis.user.event.model;

import com.solofeed.genesis.user.api.dto.UserDto;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
public class UserDisconnectionEvent extends UserEvent{
    private UserDisconnectionEvent(UserDto user){
        super(user);
    }

    public static UserDisconnectionEvent of(@NonNull UserDto user) {
        return new UserDisconnectionEvent(user);
    }
}
