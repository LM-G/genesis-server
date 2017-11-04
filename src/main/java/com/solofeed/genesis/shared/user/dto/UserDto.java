package com.solofeed.genesis.shared.user.dto;

import com.solofeed.genesis.shared.user.constant.UserRoleEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto {
    Long id;
    String name;
    String email;
    UserRoleEnum role;
    String password;
}
