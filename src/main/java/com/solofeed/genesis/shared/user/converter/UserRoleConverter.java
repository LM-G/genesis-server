package com.solofeed.genesis.shared.user.converter;

import com.solofeed.genesis.shared.user.constant.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRoleEnum, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleConverter.class);

    @Override
    public Integer convertToDatabaseColumn(UserRoleEnum role) {
        return role != null ? role.getValue() : 0;
    }

    @Override
    public UserRoleEnum convertToEntityAttribute(Integer value) {
        try {
            return UserRoleEnum.fromValue(value);
        } catch (Exception e) {
            String errorMssg = "Unknown value for " + UserRoleEnum.class.getName() + " : " + value;
            LOGGER.trace(errorMssg, e);
            throw new IllegalArgumentException(errorMssg);
        }
    }
}
