package com.solofeed.genesis.core.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Security constants
 */
@Data
@Component
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "jwt")
public class SecurityProps {
    /** Authentication type */
    private String authType;

    /** Request header which will contains the JWT to authenticate a user */
    private String header;

    /** Algorithm used for JWT encryption */
    private String algorithm;

    /** Identifies the principal that issued the JWT */
    private String issuer;

    /** String for JWT encryption */
    private String secret;

    /** String for JWT encryption */
    private Integer expireInHours;
}
