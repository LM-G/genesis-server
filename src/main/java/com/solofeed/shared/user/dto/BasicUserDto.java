package com.solofeed.shared.user.dto;

import com.solofeed.shared.user.constant.UserRoleEnum;
import lombok.Data;

@Data
public class BasicUserDto {
    Long id;
    String name;
    UserRoleEnum role;
}
