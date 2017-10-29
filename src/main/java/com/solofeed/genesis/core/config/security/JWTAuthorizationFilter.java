package com.solofeed.genesis.core.config.security;

import com.google.gson.Gson;
import com.solofeed.genesis.shared.user.dto.BasicUserDto;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authorization filter
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private SecurityProps securityProps;

    /** Initializes the filter */
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityProps securityProps) {
        super(authenticationManager);
        this.securityProps = securityProps;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // gets the authorization header content
        String header = req.getHeader(securityProps.getHeader());

        if (StringUtils.isBlank(header) || !header.startsWith(securityProps.getAuthType())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    /**
     * Reads the JWT from the Authorization header and uses it to validate the token.
     * @param request incoming request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // gets the bearer and the token
        String token = request.getHeader(securityProps.getHeader()).replace(securityProps.getAuthType(), "");
        if (StringUtils.isNotBlank(token)) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey("toto"/*securityProps.getSecret().getBytes()*/)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            if (StringUtils.isNotBlank(user)) {
                BasicUserDto userDto = (new Gson()).fromJson(user, BasicUserDto.class);
                return new UsernamePasswordAuthenticationToken(userDto.getName(), null);
            }
            return null;
        }
        return null;
    }
}
