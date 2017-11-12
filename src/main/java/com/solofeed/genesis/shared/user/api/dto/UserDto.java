package com.solofeed.genesis.shared.user.api.dto;

import com.solofeed.genesis.shared.user.domain.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String password;
    private String state;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
}
