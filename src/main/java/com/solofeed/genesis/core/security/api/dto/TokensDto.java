package com.solofeed.genesis.core.security.api.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Tokens Holder
 */
@Data
@RequiredArgsConstructor
public class TokensDto {
    /** Access token the client will need to send with every request */
    private final String accessToken;
    /** Refresh token to recreate expired token */
    private final String refreshToken;
}
