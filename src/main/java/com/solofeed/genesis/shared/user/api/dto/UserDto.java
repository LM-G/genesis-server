package com.solofeed.genesis.shared.user.api.dto;

import com.solofeed.genesis.shared.user.domain.UserRoleEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRoleEnum role;
    private String password;
}
