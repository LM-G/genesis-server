package com.solofeed.genesis.user.dao.converter;

import com.solofeed.genesis.user.domain.Role;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link UserRoleConverter}
 */
public class UserRoleConverterTest {
    private UserRoleConverter userRoleConverter;

    @Before
    public void setUp() {
        userRoleConverter = new UserRoleConverter();
    }

    @Test
    public void shouldConvertToDatabaseColumn() throws Exception {
        // execution
        Integer resultForSimple = userRoleConverter.convertToDatabaseColumn(Role.SIMPLE);
        Integer resultForManager = userRoleConverter.convertToDatabaseColumn(Role.MANAGER);
        Integer resultForAdmin = userRoleConverter.convertToDatabaseColumn(Role.ADMIN);
        Integer resultForNull = userRoleConverter.convertToDatabaseColumn(null);

        // assertions
        assertThat(resultForSimple).isEqualTo(0);
        assertThat(resultForManager).isEqualTo(1);
        assertThat(resultForAdmin).isEqualTo(2);
        assertThat(resultForNull).isEqualTo(0);
    }

    @Test
    public void shouldConvertToEntityAttribute() throws Exception {
        // execution
        Role roleSimple = userRoleConverter.convertToEntityAttribute(0);
        Role roleManager = userRoleConverter.convertToEntityAttribute(1);
        Role roleAdmin = userRoleConverter.convertToEntityAttribute(2);
        Role roleNull = userRoleConverter.convertToEntityAttribute(42);

        // assertions
        assertThat(roleSimple).isEqualByComparingTo(Role.SIMPLE);
        assertThat(roleManager).isEqualByComparingTo(Role.MANAGER);
        assertThat(roleAdmin).isEqualByComparingTo(Role.ADMIN);
        assertThat(roleNull).isNull();
    }
}