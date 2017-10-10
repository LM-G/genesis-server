package com.solofeed.shared.user.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserDto {
    @NotNull @Size(min = 6, max=64)
    private String name;
    @NotNull @Size(min = 6, max=64)
    private String email;
    @NotNull @Size(min = 8, max=64)
    private String password;
}
