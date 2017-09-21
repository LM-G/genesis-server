package com.solofeed.dto.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Create user form
 */
public class CreateUserDto {
    @NotNull @Size(min = 6, max=32)
    private String username;
    @NotNull @Size(min = 6, max=64)
    private String email;
    @NotNull @Size(min = 8, max=64)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
