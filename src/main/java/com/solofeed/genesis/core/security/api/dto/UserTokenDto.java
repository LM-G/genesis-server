package com.solofeed.genesis.core.security.api.dto;

import com.solofeed.genesis.user.domain.Role;
import lombok.Data;

/**
 * Simple user information's stored in the JWT
 */
@Data
public class UserTokenDto {
    /** User's unique id */
    private Long id;
    /** Account name */
    private String name;
    /** User role */
    private Role role;
}
