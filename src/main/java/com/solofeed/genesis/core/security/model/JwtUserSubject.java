package com.solofeed.genesis.core.security.model;

import com.solofeed.genesis.shared.user.constant.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple user information's stored in the JWT subject
 */
@Data
public class JwtUserSubject {
    protected Long id;
    protected String name;
    protected UserRoleEnum role;
}
