package com.solofeed.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import static com.solofeed.config.JerseyConfig.APPLICATION_PATH;

@Component
@ApplicationPath(APPLICATION_PATH)
public class JerseyConfig extends ResourceConfig{
    /** Application base path */
    public static final String APPLICATION_PATH = "api/v1";

    @Inject
    public JerseyConfig() {
        // register endpoints
        packages("com.solofeed.controller");
    }
}
