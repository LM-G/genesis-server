package com.solofeed.genesis.user.dao.converter;

import com.solofeed.genesis.user.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts a {@link Role} form and to database environment
 */
@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        return role != null ? role.getWeight() : 0;
    }

    @Override
    public Role convertToEntityAttribute(Integer value) {
        return Role.fromValue(value);
    }
}
