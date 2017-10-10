package com.solofeed.shared.user.constant;

import lombok.NonNull;

import java.util.Arrays;

/**
 * User's possible roles
 */
public enum UserRoleEnum {
    SIMPLE(0),
    MANAGER(1),
    ADMIN(2);

    private final int value;

    UserRoleEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static UserRoleEnum fromValue(@NonNull Integer value){
        return Arrays.stream(UserRoleEnum.values())
                .filter(e -> e.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}
