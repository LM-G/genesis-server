package com.solofeed.genesis.core.security.domain;

import com.solofeed.genesis.shared.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * Authenticated user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements Principal {

    /** Id in database*/
    private Long id;

    /** user name */
    private String name;

    /** role*/
    private Role role;

    @Override
    public String getName() {
        return name;
    }
}
