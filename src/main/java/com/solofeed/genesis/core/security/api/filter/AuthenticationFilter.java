package com.solofeed.genesis.core.security.api.filter;


import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.core.security.api.exception.AuthError;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.core.security.domain.SecurityProps;
import com.solofeed.genesis.core.security.domain.UserContext;
import com.solofeed.genesis.core.security.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

/**
 * Authentication filter for parsing all incoming request trying to reach secured resources.
 */
@Log4j2
@Provider @Secured @Priority(Priorities.AUTHENTICATION)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthenticationFilter implements ContainerRequestFilter {

    /** Security properties holder */
    private final SecurityProps securityProps;

    /** Token handling factory */
    private final TokenService tokenService;

    /** header which will be returned to client in case of unauthorized action */
    private Map<String, String> unauthorizedHeaders;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        // get the Authorization header from the request
        String header = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        // validate the Authorization header
        if (StringUtils.isBlank(header) || !header.startsWith(securityProps.getAuthScheme())) {
            throw AuthError.ofMissingJWT(unauthorizedHeaders);
        }

        // gets the token chain
        String token = header.replace(securityProps.getAuthScheme(), StringUtils.EMPTY).trim();

        // validate the presence of the JWT
        if(StringUtils.isBlank(token)) {
            throw AuthError.ofMissingJWT(unauthorizedHeaders);
        }

        UserTokenDto userFromToken;

        try {
            // validates and extract the user subject from the token
            userFromToken = tokenService.extractUser(token);
        } catch (ExpiredJwtException e) {
            throw AuthError.ofExpiredJWT(e.getMessage(), unauthorizedHeaders);
        } catch (Exception e) {
            throw AuthError.ofInvalidJWT(e.getMessage(), unauthorizedHeaders);
        }

        // sets the authenticated user in security context
        CurrentUser currentUser = new CurrentUser(userFromToken.getId(), userFromToken.getName(), userFromToken.getRole());
        UserContext userContext = new UserContext(currentUser, securityProps.getAuthScheme());
        context.setSecurityContext(userContext);
    }

    /**
     * Reads header from {@link SecurityProps} values
     */
    @PostConstruct
    private void setUpHeaders(){
        // Sets the WWW-Authenticate header
        this.unauthorizedHeaders = ImmutableMap.of(
                HttpHeaders.WWW_AUTHENTICATE,
                securityProps.getAuthScheme() + " realm=\"" + securityProps.getRealm() + "\""
        );
    }
}
