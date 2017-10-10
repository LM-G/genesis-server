package com.solofeed.core.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationConfig;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;
import org.springframework.stereotype.Component;

import javax.validation.ParameterNameProvider;
import javax.validation.Validation;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
