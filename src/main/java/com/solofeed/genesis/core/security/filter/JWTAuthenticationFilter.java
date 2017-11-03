package com.solofeed.genesis.core.security.filter;

import com.google.gson.Gson;
import com.solofeed.genesis.core.config.security.SecurityProps;
import com.solofeed.genesis.core.security.auth.model.JwtUser;
import com.solofeed.genesis.core.security.auth.service.CypherService;
import com.solofeed.genesis.shared.user.dto.BasicUserDto;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authorization filter
 */
@Log4j2
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Inject
    private AuthenticationManager authenticationManager;

    @Inject
    private SecurityProps securityProps;

    @Inject
    private Gson gson;

    @Inject
    private CypherService cypherService;

    @Inject
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {
        // gets the authorization headerName content
        String header = req.getHeader(securityProps.getHeaderName());

        if (StringUtils.isNotBlank(header) && header.startsWith(securityProps.getGrantType())) {
            handleAuthentication(header);
        }

        chain.doFilter(req, res);
    }

    /**
     * Reads the JWT from the Authorization headerName and uses it to authenticate the user.
     * @param header incoming request headerName
     */
    private void handleAuthentication(String header) {
        // gets the bearer and the token
        String token = header.replace(securityProps.getGrantType(), StringUtils.EMPTY);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (StringUtils.isNotBlank(token)) {
            // parse the token and gets the user
            BasicUserDto user = cypherService.getUserSubjectFromToken(token);

            if (user != null && securityContext.getAuthentication() == null) {
                JwtUser authenticatedUser = JwtUser.class.cast(userDetailsService.loadUserByUsername(user.getName()));
                Authentication auth = authenticationManager.authenticate(null/*token*/);
                securityContext.setAuthentication(auth);
            }
        }
    }
}
