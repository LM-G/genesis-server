package com.solofeed.core.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignUpDto {
    @NotNull @Size(min = 6, max=64)
    private String username;
    @NotNull @Size(min = 6, max=64)
    private String email;
    @NotNull @Size(min = 8, max=64)
    private String password;
}

