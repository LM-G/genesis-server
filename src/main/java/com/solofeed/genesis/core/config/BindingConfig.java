package com.solofeed.genesis.core.config;

import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.core.provider.CurrentUserProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

/**
 * Custom bindings configuration
 */
public class BindingConfig extends AbstractBinder {
    @Override
    protected void configure() {
        // provide CurrentUser in all requests scopes, CurrentUser is accessible directly via Jersey Context
        bindFactory(CurrentUserProvider.class)
                .proxy(true)
                .to(CurrentUser.class)
                .in(RequestScoped.class);
    }
}
