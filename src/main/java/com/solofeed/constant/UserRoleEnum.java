package com.solofeed.constant;

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
}
