package com.solofeed.genesis.shared.user.dao.converter;

import com.solofeed.genesis.shared.user.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<Role, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleConverter.class);

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        return role != null ? role.getWeight() : 0;
    }

    @Override
    public Role convertToEntityAttribute(Integer value) {
        try {
            return Role.fromValue(value);
        } catch (Exception e) {
            String errorMssg = "Unknown value for " + Role.class.getName() + " : " + value;
            LOGGER.trace(errorMssg, e);
            throw new IllegalArgumentException(errorMssg);
        }
    }
}
