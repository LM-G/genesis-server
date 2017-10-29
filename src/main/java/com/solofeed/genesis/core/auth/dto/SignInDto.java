package com.solofeed.genesis.core.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignInDto {
    @NotNull @Size(min = 6, max=64)
    private String login;
    @NotNull @Size(min = 8, max=64)
    private String password;
    private Boolean rememberme;
}
