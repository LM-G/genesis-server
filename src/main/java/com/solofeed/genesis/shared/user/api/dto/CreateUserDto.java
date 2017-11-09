package com.solofeed.genesis.shared.user.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Dto de creation d'un nouvel utilisateur
 */
@Data
public class CreateUserDto {
    @NotNull @Size(min = 6, max=64)
    private String name;
    @NotNull
    @Size(min = 6, max=64)
    @Email
    private String email;
    @NotNull @Size(min = 8, max=64)
    private String password;
}
