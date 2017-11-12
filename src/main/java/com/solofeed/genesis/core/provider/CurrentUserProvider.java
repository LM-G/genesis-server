package com.solofeed.genesis.core.provider;

import com.solofeed.genesis.core.security.domain.CurrentUser;
import org.glassfish.hk2.api.Factory;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Extract {@link CurrentUser} from current {@link SecurityContext} and provide it.
 */
public class CurrentUserProvider implements Factory<CurrentUser> {
    /** Current security context */
    @Context
    private SecurityContext context;

    @Override
    public CurrentUser provide() {
        return CurrentUser.class.cast(context.getUserPrincipal());
    }

    @Override
    public void dispose(CurrentUser currentUser) {
        // no-op
    }
}
