package com.solofeed.utilities;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Utilities for test initializations
 */
public class InitializationUtils {

    /**
     * Execute all methods annotated with @{@link PostConstruct} inside a given component
     * @param component component to postconstruct
     * @param params postconstruct params
     * @param <T> type of component
     */
    public static <T> void postConstruct(T component, Object ...params) {
        // get the post construct method
        Method method = Arrays.stream(component.getClass().getDeclaredMethods())
            .filter(m -> m.getDeclaredAnnotation(PostConstruct.class) != null)
            .findFirst().orElseThrow(() -> new RuntimeException("Can't post construct"));

        try {
            if(method.isAccessible()){
                method.invoke(component, params);
            } else {
                method.setAccessible(true);
                method.invoke(component, params);
                method.setAccessible(false);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't post construct", e);
        }
    }
}
