package com.solofeed.genesis.core.security.domain;

import com.solofeed.genesis.shared.user.domain.UserRoleEnum;
import lombok.Data;

import java.security.Principal;

@Data
public class CurrentUser implements Principal {
    private Long id;
    private String username;
    private UserRoleEnum role;
    @Override
    public String getName() {
        return null;
    }
}
