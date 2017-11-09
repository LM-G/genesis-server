package com.solofeed.genesis.core.security.api.filter;


import com.solofeed.genesis.core.security.domain.SecurityProps;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.api.exception.AuthException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Log4j2
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter{
    @Inject
    private SecurityProps securityProps;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        // get the Authorization header from the request
        String header = context.getHeaderString(HttpHeaders.AUTHORIZATION);

        // validate the Authorization header
        if (StringUtils.isBlank(header) || !header.startsWith(securityProps.getAuthScheme())) {
            throw AuthException.ofMissingJWT();
        }

        // gets the token chain
        String token = header.replace(securityProps.getAuthScheme(), StringUtils.EMPTY).trim();

        try {
            validateToken(token);
        } catch (Exception e) {
            LOGGER.info("Pas bien");
        }

        final SecurityContext currentSecurityContext = context.getSecurityContext();
        context.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {
                    @Override
                    public String getName() {
                        return "Loulou";
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return securityProps.getAuthScheme();
            }
        });
    }

    /**
     * Abort the filter chain with a 401 status code response
     * he WWW-Authenticate header is sent along with the response
     * @param context
     */
    private void abortWithUnauthorized(ContainerRequestContext context) {
        context.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,
                    securityProps.getAuthScheme() + " realm=\"" + securityProps.getRealm() + "\"")
                .build());
    }


    /**
     * Check if the token was issued by the server and if it's not expired
     * @param token token to check
     * @throws Exception if the token is invalid
     */
    private void validateToken(String token) throws Exception {
        //
        // Throw an Exception
    }
}
