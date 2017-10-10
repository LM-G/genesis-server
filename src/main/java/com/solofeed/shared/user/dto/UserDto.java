package com.solofeed.shared.user.dto;

import com.solofeed.shared.user.constant.UserRoleEnum;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String username;
    String email;
    UserRoleEnum role;
    String password;
}
