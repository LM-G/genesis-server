package com.solofeed.genesis.core.auth.service;

import com.google.gson.Gson;
import com.solofeed.genesis.core.config.security.SecurityProps;
import com.solofeed.genesis.shared.user.dto.BasicUserDto;
import com.solofeed.genesis.shared.user.dto.UserDto;
import com.solofeed.genesis.shared.user.mapper.UserMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

/**
 * Authentication service
 */
@Service
public class CypherService {

    /** JSON builder */
    @Inject private Gson gson;

    /** User DTO-Entity converter */
    @Inject private UserMapper userMapper;

    /** Security properties */
    @Inject private SecurityProps securityProps;

    /**
     * Create a JWT token for the provided user
     * @param userDto user data
     * @return JWT token
     */
    public String createToken(UserDto userDto) {
        // only stores vital minimal data in JWT
        BasicUserDto basicUserDto = userMapper.toBasicUserDto(userDto);
        String jsonifiedBasicUser = gson.toJson(basicUserDto);
        LocalDateTime now = LocalDateTime.now();

        // gets the key
        Key signingKey = getKey();

        // set dates
        Date issuedAt = Date.from(now.toInstant(ZoneOffset.UTC));
        Date expiration = Date.from(now.plusHours(securityProps.getExpireInHours()).toInstant(ZoneOffset.UTC));

        // jwt claim setting
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(jsonifiedBasicUser)
                .setIssuedAt(issuedAt)
                .setIssuer(securityProps.getIssuer())
                .signWith(SignatureAlgorithm.forName(securityProps.getAlgorithm()), "toto")
                .setExpiration(expiration);

        return jwtBuilder.compact();
    }

    /**
     * Extract user basic information from a JWT token
     * @param token token to be scanned
     * @return user basic information
     */
    public BasicUserDto extractUser(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
        String jsonifiedBasicUser = claims.getBody().getSubject();
        return gson.fromJson(jsonifiedBasicUser, BasicUserDto.class);
    }

    /**
     * Generates a new encrypted key
     * @return encrypted key with secret and algorihtm set in security settings
     */
    private Key getKey(){
        byte[] encodedSecret = Base64.getEncoder().encode(securityProps.getSecret().getBytes());
        return new SecretKeySpec(encodedSecret, SignatureAlgorithm.forName(securityProps.getAlgorithm()).getJcaName());
    }
}
