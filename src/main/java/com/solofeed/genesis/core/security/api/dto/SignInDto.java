package com.solofeed.genesis.core.security.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Sign in form content
 */
@Data
public class SignInDto {

    /** Account name or email adress */
    @NotNull @Size(min = 6, max=64)
    private String login;

    /** password */
    @NotNull @Size(min = 8, max=64)
    private String password;

    // todo remove this shit
    private Boolean rememberme;
}
