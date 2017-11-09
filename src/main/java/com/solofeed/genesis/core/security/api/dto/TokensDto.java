package com.solofeed.genesis.core.security.api.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokensDto {
    private final String accessToken;
    private final String refreshToken;
}
