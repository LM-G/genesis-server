package com.solofeed.genesis.core.security.model.token;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessToken implements Token{
    private final String token;
    private Claims claims;

    @Override
    public String getToken() {
        return this.token;
    }

    public Claims getClaims() {
        return claims;
    }
}
