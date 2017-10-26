package com.solofeed.core.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

import static com.solofeed.core.config.ApplicationConfig.APPLICATION_PATH;

@Component
@ApplicationPath(APPLICATION_PATH)
public class ApplicationConfig extends ResourceConfig{
    /** Application base path */
    public static final String APPLICATION_PATH = "api/v1";
    /** Endpoints location */
    public static final String ENDPOINTS = "com.solofeed";

    public ApplicationConfig() {
        // register endpoints
        packages(ENDPOINTS);
    }
}
