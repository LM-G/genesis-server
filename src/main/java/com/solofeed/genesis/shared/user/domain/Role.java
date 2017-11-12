package com.solofeed.genesis.shared.user.domain;

import lombok.NonNull;

import java.util.Arrays;

/**
 * User's roles to determine security permissions
 */
public enum Role {
    /** Normal user */
    SIMPLE(0),
    /** Game master */
    MANAGER(1),
    /** Administrator */
    ADMIN(2);

    /**
     * Role weight, the heavier it is, the better. The weight is stored in database, not the stringified constant
     */
    private final int weight;

    Role(int weight){
        this.weight = weight;
    }

    public int getWeight(){
        return this.weight;
    }

    /**
     * Determines a constant from a database weight
     * @param value weight from database
     * @return matching constant or null if incorrect weight
     */
    public static Role fromValue(@NonNull Integer value){
        return Arrays.stream(Role.values())
                .filter(e -> e.getWeight() == value)
                .findFirst()
                .orElse(null);
    }
}
