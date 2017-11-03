package com.solofeed.genesis.core.config;

import com.solofeed.genesis.core.config.converter.BodyHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

import static com.solofeed.genesis.core.config.ApplicationConfig.APPLICATION_PATH;

@Configuration
@EnableConfigurationProperties
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
