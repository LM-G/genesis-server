package com.solofeed.genesis.core.security.auth.service;

import com.google.gson.Gson;
import com.solofeed.genesis.core.config.security.SecurityProps;
import com.solofeed.genesis.shared.user.dto.BasicUserDto;
import com.solofeed.genesis.shared.user.dto.UserDto;
import com.solofeed.genesis.shared.user.mapper.UserMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.function.Function;

/**
 * Token handling service service
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
    public String generateToken(UserDto userDto) {
        // only stores vital minimal data in JWT
        BasicUserDto basicUserDto = userMapper.toBasicUserDto(userDto);
        String jsonifiedBasicUser = gson.toJson(basicUserDto);
        LocalDateTime now = LocalDateTime.now();

        // set dates
        Date issuedAt = Date.from(now.toInstant(ZoneOffset.UTC));
        Date expiration = Date.from(now.plusHours(securityProps.getExpireInHours()).toInstant(ZoneOffset.UTC));

        // jwt claim setting
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(jsonifiedBasicUser)
                .setIssuedAt(issuedAt)
                .setIssuer(securityProps.getIssuer())
                .signWith(SignatureAlgorithm.forName(securityProps.getAlgorithm()), securityProps.getKey())
                .setExpiration(expiration);

        return jwtBuilder.compact();
    }

    /**
     * Extract user basic information from a JWT token
     * @param token token to be scanned
     * @return user basic information
     */
    public BasicUserDto getUserSubjectFromToken(String token) {
        String jsonifiedBasicUser = getClaimFromToken(token, Claims::getSubject);
        return gson.fromJson(jsonifiedBasicUser, BasicUserDto.class);
    }

    /**
     * Get the token's creation date
     * @param token token to parse
     * @return date creation
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    /**
     * Get a claim from a token
     * @param token token to parse
     * @param claimsResolver claim getter
     * @param <T> claim type
     * @return the wanted claim
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Check if the token is expired
     * @param token token to parse
     * @return true if the token is expired, else returns false
     */
    public Boolean isTokenExpired(String token) {
        Date issuedAt = getIssuedAtDateFromToken(token);
        LocalDateTime date = LocalDateTime.ofInstant(issuedAt.toInstant(), ZoneOffset.UTC);
        return date.plusHours(securityProps.getExpireInHours()).isBefore(LocalDateTime.now());
    }

    /**
     * Check if the token is not expired
     * @param token token to parse
     * @return true if the token is not expired, else returns false
     */
    public Boolean isTokenNotExpired(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Get all claims from the token
     * @param token token to parse
     * @return all the claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(securityProps.getKey())
            .parseClaimsJws(token)
            .getBody();
    }


}
