package com.solofeed.genesis.shared.user.dto;

import com.solofeed.genesis.shared.user.constant.UserRoleEnum;
import lombok.Data;

@Data
public class BasicUserDto {
    Long id;
    String name;
    UserRoleEnum role;
}
