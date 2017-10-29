package com.solofeed.genesis.shared.user.dto;

import com.solofeed.genesis.shared.user.constant.UserRoleEnum;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String name;
    String email;
    UserRoleEnum role;
    String password;
}
