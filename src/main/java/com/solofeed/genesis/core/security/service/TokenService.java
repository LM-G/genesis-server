package com.solofeed.genesis.core.security.service;

import com.google.gson.Gson;
import com.solofeed.genesis.core.security.api.dto.TokensDto;
import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.core.security.domain.SecurityProps;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.mapper.UserMapper;
import com.solofeed.genesis.util.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

/**
 * Create access and refresh tokens
 */
@Component
@RequiredArgsConstructor
public class TokenService {

    /** JSON builder */
    private final Gson gson;

    /** User DTO-Entity converter */
    private final UserMapper userMapper;

    /** Security properties */
    private final SecurityProps securityProps;

    /**
     * Creates a token holder containing both access and refresh token
     * @param userDto user data
     * @return token holder containing access and refresh tokens
     */
    public TokensDto createTokens(UserDto userDto){
        UserTokenDto user = userMapper.toSubjectDto(userDto);
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user, userDto.getState());
        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * Get the token's creation date
     * @param token token to parse
     * @return date creation
     */
    public UserTokenDto extractUser(String token) {
        String jsonifiedBasicUser = getClaimFromToken(token, Claims::getSubject);
        return gson.fromJson(jsonifiedBasicUser, UserTokenDto.class);
    }

    /**
     * Create an acess token for the user subject
     * @param user provided user
     * @param identifier if the JWT must be a refresh token
     * @return JWT
     */
    private String createToken(UserTokenDto user, String identifier) {
        boolean isRefreshToken = StringUtils.isNotBlank(identifier);
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();

        // stringify the user
        String jsonifiedBasicUser = gson.toJson(user);

        // set dates
        Date issuedAt = DateUtils.toDate(now, zone);
        Date expiration = isRefreshToken ?
                DateUtils.toDate(now.plusMonths(securityProps.getExpireInMonth()), zone) :
                DateUtils.toDate(now.plusMinutes(securityProps.getExpireInMinutes()), zone);

        // jwt claim setting
        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(jsonifiedBasicUser)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .setIssuer(securityProps.getIssuer())
            .signWith(SignatureAlgorithm.forName(securityProps.getAlgorithm()), securityProps.getKey());

        if(isRefreshToken) {
            jwtBuilder.setId(identifier);
        }

        return jwtBuilder.compact();
    }

    /**
     * Generate a new access token and refresh the refresh token if it needs to be refreshed. Refresh tokens are
     * refreshed (we generate a new one) when there is only one day remaining before their
     * expiration date
     * @param token refresh token
     * @return tokens holder with both access and refresh token
     */
    public TokensDto refresh(String token) {
        // get all claims from refresh token
        Claims claims = getClaimsFromToken(token);
        ZoneId zone = ZoneId.systemDefault();
        UserTokenDto user = extractUser(token);
        String refreshedToken = token;

        // get the date on day before expiration of the refresh token
        LocalDateTime dayBeforeExpiration = LocalDateTime.ofInstant(claims.getExpiration().toInstant(), zone).minusDays(1);

        // postpone expiration date if we are less than one day before expiration
        if(LocalDateTime.now().isAfter(dayBeforeExpiration)){
            refreshedToken = createRefreshToken(user, claims.getId());
        }

        String accessToken = createAccessToken(user);

        return new TokensDto(accessToken, refreshedToken);
    }

    /**
     * Get the identifier "jti" from a token
     * @param token token to parse
     * @return token identifier
     */
    public String getIdentifier(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    /**
     * Create a short lived JWT for a given user
     * @param user base of wanted JWT
     * @return access JWT for the provided user
     */
    private String createAccessToken(@NonNull UserTokenDto user) {
        return createToken(user, null);
    }

    /**
     * Create a long lived JWT for a given user
     * @param user user information to store in JWT subject
     * @param identifier id matching the last up to date state of the user in database
     * @return refresh JWT for the provided user
     */
    private String createRefreshToken(@NonNull UserTokenDto user, String identifier) {
        return createToken(user, identifier);
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
