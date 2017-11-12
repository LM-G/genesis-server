package com.solofeed.genesis.core.security.domain;

import com.solofeed.genesis.shared.user.domain.Role;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Data
@Builder
@RequiredArgsConstructor
public class UserContext implements SecurityContext {
    /** Authenticated user */
    private final CurrentUser user;
    private final String authenticationScheme;

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    /**
     * Check if the user contains the allowed role
     * @param role targeted role
     * @return true if the user has the role, else false
     */
    public boolean isUserInRole(Role role) {
        return user.getRole().getWeight() >= role.getWeight();
    }

    @Override
    public boolean isSecure() {
        return user != null;
    }

    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
}
