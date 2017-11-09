package com.solofeed.genesis.core.security.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {
    private Jws<Claims> claims;

    /**
     * Creates and validates Refresh token
     * TODO utiliser un composant / factory dédié et pas un domain ..
     */
    public static Optional<RefreshToken> create(Jws<Claims> claims) {
        // get the scopes
        /*
        List<String> scopes = claims.getBody().get("scopes", List.class);
        if (scopes == null || scopes.isEmpty()
                || !scopes.stream().filter(scope -> Scope.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent()) {
            return Optional.empty();
        }*/

        return Optional.of(new RefreshToken(claims));
    }

    public String getJti() {
        return claims.getBody().getId();
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
