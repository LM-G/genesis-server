package com.solofeed.genesis.user.event.model;

import com.solofeed.genesis.user.api.dto.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;


@EqualsAndHashCode(callSuper = true)
public class UserConnectionEvent extends UserEvent{
    private UserConnectionEvent(UserDto user){
        super(user);
    }

    public static UserConnectionEvent of(@NonNull UserDto user) {
        return new UserConnectionEvent(user);
    }
}
