package com.solofeed.genesis.core.security.jwt;

import com.solofeed.genesis.core.security.auth.service.CypherService;
import com.solofeed.genesis.core.security.model.JwtUserSubject;
import com.solofeed.genesis.core.security.model.UserContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of JwtToken to perform authentication.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider{
    /** Token cypher service */
    @Inject
    private CypherService cypherService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // get the token from jwt authentication
        String token = String.class.cast(authentication.getCredentials());
        JwtUserSubject subject = cypherService.getUserSubjectFromToken(token);
        // temp : only one authority by user
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(subject.getRole().authority()));
        // creates the user context
        UserContext userContext = UserContext.create(subject.getName(), authorities);
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
