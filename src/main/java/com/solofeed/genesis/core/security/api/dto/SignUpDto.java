package com.solofeed.genesis.core.security.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Registration form content
 */
@Data
public class SignUpDto {

    /** Account's name */
    @NotNull @Size(min = 6, max=64)
    private String name;

    /** Email address */
    @NotNull @Size(min = 6, max=64)
    private String email;

    /** Account's initial password */
    @NotNull @Size(min = 8, max=64)
    private String password;
}

