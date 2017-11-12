package com.solofeed.genesis.core.security.domain;

import com.solofeed.genesis.shared.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
public class CurrentUser implements Principal {
    private Long id;
    private String name;
    private Role role;
    @Override
    public String getName() {
        return name;
    }
}
