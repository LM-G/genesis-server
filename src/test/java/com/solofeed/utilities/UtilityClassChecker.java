package com.solofeed.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Utility class validator. It is meaningless on feature level but i want that all my utility classes to actually
 * be utility classes !
 */
public class UtilityClassChecker {
    public static void check(Class<?> type) throws Exception {
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);

        // execution
        assertThatThrownBy(() -> constructor.newInstance())
            // assertions
            .isInstanceOf(InvocationTargetException.class)
            .hasCauseExactlyInstanceOf(UnsupportedOperationException.class);
    }
}
