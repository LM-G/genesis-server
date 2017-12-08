package com.solofeed.genesis.user.event;

import com.solofeed.genesis.core.AppState;
import com.solofeed.genesis.user.domain.User;
import com.solofeed.genesis.user.event.model.UserConnectionEvent;
import com.solofeed.genesis.user.event.model.UserDisconnectionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Handle's {@link User} state
 */
@Service
@RequiredArgsConstructor
public class UserEventHandler {
    /** Main app state */
    private final AppState state;

    /**
     * Listen user connection events
     * @param event user connection event
     */
    @EventListener
    public void onUserConnected(UserConnectionEvent event) {
        state.addConnectedUser(event.getUser());
    }

    /**
     * Listen user disconnection events
     * @param event user disconnection event
     */
    @EventListener
    public void onUserDisConnected(UserDisconnectionEvent event) {
        state.removeConnectedUser(event.getUser());
    }
}
