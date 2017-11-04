package com.solofeed.genesis.core.security.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Authenticated user context
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContext {
    private final String username;
    private final List<GrantedAuthority> authorities;

    public static UserContext create(String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) {
            // TODO uniformize exception
            throw new IllegalArgumentException("Username is blank: " + username);
        }
        return new UserContext(username, authorities);
    }
}
