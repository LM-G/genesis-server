package com.solofeed.shared.user.dto;

import com.solofeed.shared.user.constant.UserRoleEnum;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String name;
    String email;
    UserRoleEnum role;
    String password;
}
