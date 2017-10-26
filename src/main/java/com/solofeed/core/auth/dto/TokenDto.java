package com.solofeed.core.auth.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    @SerializedName("access_token")
    private String accessToken;
}
