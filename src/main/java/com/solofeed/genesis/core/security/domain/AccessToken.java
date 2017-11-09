package com.solofeed.genesis.core.security.domain;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessToken {
    private final String token;
    private Claims claims;
}
