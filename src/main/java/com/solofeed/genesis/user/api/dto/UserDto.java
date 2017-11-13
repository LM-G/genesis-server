package com.solofeed.genesis.user.api.dto;

import com.solofeed.genesis.user.domain.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private transient String password;
    private transient String state;
    private transient LocalDate createdAt;
    private transient LocalDateTime updatedAt;
}
