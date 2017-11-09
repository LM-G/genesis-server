package com.solofeed.genesis.core.security.service;

import com.google.gson.Gson;
import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.domain.SecurityProps;
import com.solofeed.genesis.core.security.api.dto.UserSubjectDto;
import com.solofeed.genesis.core.security.api.dto.TokensDto;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.mapper.UserMapper;
import com.solofeed.genesis.shared.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.function.Function;

/**
 * Create access and refresh tokens
 */
@Component
@RequiredArgsConstructor
public class TokenFactory {
    private static final String USER_STATE_ID = "state_id";

    /** JSON builder */
    private final Gson gson;

    /** User DTO-Entity converter */
    private final UserMapper userMapper;

    /** Security properties */
    private final SecurityProps securityProps;

    /** User service */
    private final UserService userService;

    private String toto;

    /**
     * Create a short lived JWT for a given user
     * @param user base of wanted JWT
     * @return access JWT for the provided user
     */
    public String createAccessToken(@NonNull UserSubjectDto user) {
        return createToken(user);
    }

    /**
     * Create a long lived JWT for a given user
     * @param user user information to store in JWT subject
     * @return refresh JWT for the provided user
     */
    public String createRefreshToken(@NonNull UserSubjectDto user) {
        return createToken(user, true);
    }

    public TokensDto createTokens(UserDto userDto){
        UserSubjectDto user = userMapper.toBasicUserDto(userDto);
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);
        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * Get the token's creation date
     * @param token token to parse
     * @return date creation
     */
    public UserSubjectDto extractUser(String token) {
        String jsonifiedBasicUser = getClaimFromToken(token, Claims::getSubject);
        return gson.fromJson(jsonifiedBasicUser, UserSubjectDto.class);
    }

    /**
     * Validate a token validity
     * @param token token to parse
     * @return extracted claims
     * @throws APIException if user is not found
     */
    public Jws<Claims> validate(String token) throws APIException {
        JwtParser parser = Jwts.parser().setSigningKey(securityProps.getKey());
        UserSubjectDto jwtUser = extractUser(token);
        UserDto user = userService.getUser(jwtUser.getId());
        // TODO utiliser userDetails et vérifier isBanned, isActive, etc.
        return parser.require(USER_STATE_ID, user.getPassword()).parseClaimsJws(token);
    }

    /**
     * Create an acess token for the user subject
     * @param user provided user
     * @return
     */
    private String createToken(UserSubjectDto user) {
        return createToken(user, false);
    }

    private String createToken(UserSubjectDto user, boolean isRefreshToken) {
        LocalDateTime now = LocalDateTime.now();
        String jsonifiedBasicUser = gson.toJson(user);
        // set dates
        Date issuedAt = Date.from(now.toInstant(ZoneOffset.UTC));
        Date expiration = isRefreshToken ?
            Date.from(now.plusMonths(securityProps.getExpireInMonth()).toInstant(ZoneOffset.UTC)) :
            Date.from(now.plusMinutes(securityProps.getExpireInMinutes()).toInstant(ZoneOffset.UTC));

        // jwt claim setting
        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(jsonifiedBasicUser)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .setIssuer(securityProps.getIssuer())
            .signWith(SignatureAlgorithm.forName(securityProps.getAlgorithm()), securityProps.getKey());

        return jwtBuilder.compact();
    }

    /**
     * Get a claim from a token
     * @param token token to parse
     * @param claimsResolver claim getter
     * @param <T> claim type
     * @return the wanted claim
     */
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
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
