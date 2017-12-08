package com.solofeed.genesis.user.event.model;

import com.solofeed.genesis.user.api.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** User related event */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class UserEvent {
    /** User link to the event */
    protected UserDto user;
}
