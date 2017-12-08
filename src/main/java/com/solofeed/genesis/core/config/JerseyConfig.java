package com.solofeed.genesis.core.config;

import com.solofeed.genesis.core.AppState;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

import static com.solofeed.genesis.core.config.JerseyConfig.APPLICATION_PATH;

@Configuration
@ApplicationPath(APPLICATION_PATH)
public class JerseyConfig extends ResourceConfig{
    /** Application base path */
    static final String APPLICATION_PATH = "api/v1";
    /** Endpoints location */
    private static final  String ENDPOINTS = "com.solofeed.genesis";

    public JerseyConfig() {
        // register endpoints
        packages(ENDPOINTS);

        // register custom bindings
        register(new BindingConfig());

        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    @Bean
    public AppState appState(){
        return new AppState();
    }
}
