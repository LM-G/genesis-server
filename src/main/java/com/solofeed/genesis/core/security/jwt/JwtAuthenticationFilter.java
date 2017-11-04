package com.solofeed.genesis.core.security.jwt;

import com.solofeed.genesis.core.security.auth.exception.AuthException;
import com.solofeed.genesis.core.security.config.SecurityProps;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication filter
 * </p>
 * Check for access token in X-Authorization header and delegate it the JwtAuthenticationProvider.
 * Then it invokes success or failure strategies based on the outcome of JwtAuthenticationProvider result.
 */
@Log4j2
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /** Security properties */
    private final SecurityProps securityProps;

    @Inject
    public JwtAuthenticationFilter(SecurityProps securityProps, RequestMatcher matcher) {
        super(matcher);
        this.securityProps = securityProps;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        // gets the authorization headerName content
        String header = req.getHeader(securityProps.getHeaderName());

        if (StringUtils.isBlank(header) || !header.startsWith(securityProps.getGrantType())) {
            throw AuthException.ofMissingJWT();
        }

        // gets the token chain
        String token = header.replace(securityProps.getGrantType(), StringUtils.EMPTY);

        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(token);

        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
    }
}
