package com.solofeed.genesis.core.security.api.filter;

import com.solofeed.genesis.core.security.api.exception.AuthError;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.domain.UserContext;
import com.solofeed.genesis.user.domain.Role;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * Authorization filter to check user permissions before accessing a resource
 */
@Secured @Provider @Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        // Get the resource class which matches with the requested URL and extract the role declared by it
        Class<?> resourceClass = resourceInfo.getResourceClass();
        Role roleFromClass = extractRole(resourceClass);

        // Get the resource method which matches with the requested URL and extract the role declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        Role rolesFromMethod = extractRole(resourceMethod);

        // get the user role
        UserContext userContext = UserContext.class.cast(context.getSecurityContext());

        // Check if the user is allowed to execute the method
        // The method annotations override the class annotations
        boolean accessGranted = true;

        if(rolesFromMethod != null) {
            accessGranted = userContext.isUserInRole(rolesFromMethod);
        } else if( roleFromClass != null) {
            accessGranted = userContext.isUserInRole(roleFromClass);
        }

        if(!accessGranted) {
            throw AuthError.ofInsufficientPermission();
        }
    }

    /**
     * Extract the role from the annotated element
     * @param annotatedElement element to check
     * @return role
     */
    private Role extractRole(AnnotatedElement annotatedElement) {
        Role role = null;
        if (annotatedElement != null) {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            role = secured != null ? secured.value() : null;
        }
        return role;
    }
}
