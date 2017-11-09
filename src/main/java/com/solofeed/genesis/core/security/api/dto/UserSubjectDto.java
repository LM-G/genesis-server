package com.solofeed.genesis.core.security.api.dto;

import com.solofeed.genesis.shared.user.domain.UserRoleEnum;
import lombok.Data;

/**
 * Simple user information's stored in the JWT subject
 */
@Data
public class UserSubjectDto {
    protected Long id;
    protected String username;
    protected UserRoleEnum role;
}
