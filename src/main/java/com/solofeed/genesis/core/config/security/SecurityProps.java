package com.solofeed.genesis.core.config.security;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Security constants
 */
@Data
@Component
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "jwt")
public class SecurityProps {
    /** Authentication type */
    private String grantType;

    /** Request header name which will contains the JWT to authenticate a user */
    private String headerName;

    /** Algorithm used for JWT encryption */
    private String algorithm;

    /** Identifies the principal that issued the JWT */
    private String issuer;

    /** String for JWT encryption */
    private String secret;

    /** String for JWT encryption */
    private Integer expireInHours;

    /** Encoded generated key from secret */
    private Key key;

    @PostConstruct
    public void init(){
        this.key = generateKey();
    }

    /**
     * Generates a new encrypted key
     * @return encrypted key with secret and algorihtm set in security settings
     */
    private Key generateKey(){
        byte[] encodedSecret = Base64.getEncoder().encode(secret.getBytes());
        return new SecretKeySpec(encodedSecret, SignatureAlgorithm.forName(algorithm).getJcaName());
    }
}
