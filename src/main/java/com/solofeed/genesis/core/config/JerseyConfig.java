package com.solofeed.genesis.core.config;

import com.solofeed.genesis.core.provider.CurrentUserProvider;
import com.solofeed.genesis.core.provider.MessageBodyProvider;
import com.solofeed.genesis.core.security.api.filter.AuthenticationFilter;
import com.solofeed.genesis.core.security.api.filter.AuthorizationFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

import static com.solofeed.genesis.core.config.JerseyConfig.APPLICATION_PATH;

@Configuration
@ApplicationPath(APPLICATION_PATH)
public class JerseyConfig extends ResourceConfig{
    /** Application base path */
    public static final String APPLICATION_PATH = "api/v1";
    /** Endpoints location */
    public static final String ENDPOINTS = "com.solofeed.genesis";

    public JerseyConfig() {
        // register endpoints
        packages(ENDPOINTS);

        // register custom bindings
        register(new BindingConfig());
    }
}
