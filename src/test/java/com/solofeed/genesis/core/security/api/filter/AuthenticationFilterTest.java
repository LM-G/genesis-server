package com.solofeed.genesis.core.security.api.filter;

import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.core.security.domain.SecurityProps;
import com.solofeed.genesis.core.security.domain.UserContext;
import com.solofeed.genesis.core.security.service.TokenService;
import com.solofeed.genesis.user.domain.Role;
import com.solofeed.utilities.InitializationUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link AuthenticationFilter}
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {
    private static final Map<String, String> HEADERS = ImmutableMap.of("WWW-Authenticate","Bearer realm=\"foo\"");
    private static final String SOME_JWT = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
        "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9." +
        "TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";

    private AuthenticationFilter authenticationFilter;

    @Mock
    private TokenService tokenService;

    @Mock
    private ContainerRequestContext context;

    private SecurityProps securityProps;

    @Before
    public void setUp() {
        securityProps = new SecurityProps();
        securityProps.setAuthScheme("Bearer");
        securityProps.setRealm("foo");

        // filter creation
        authenticationFilter = new AuthenticationFilter(securityProps, tokenService);

        // post constructing
        InitializationUtils.postConstruct(authenticationFilter);
    }

    @Test
    public void shouldFailWithMissingJWTErrorBecauseNoHeaderProvided() throws Exception {
        failWithMissingJWT(null);
    }

    @Test
    public void shouldFailWithMissingJWTErrorBecauseWrongAuthScheme() throws Exception {
        failWithMissingJWT("foo bar");
    }

    @Test
    public void shouldFailWithMissingJWTErrorBecauseNoTokenProvided(){
        failWithMissingJWT("Bearer ");
    }


    @Test
    public void shouldFailWithExpiredJWTError(){
        // stubbing
        when(context.getHeaderString(anyString())).thenReturn(SOME_JWT);
        when(tokenService.extractUser(anyString())).thenThrow(new ExpiredJwtException(null, null, "foo"));

        // execution
        assertThatThrownBy(() -> authenticationFilter.filter(context))
            // assertions
            .isInstanceOf(SecurityException.class)
            .hasNoCause()
            .hasMessage("foo")
            .extracting("code", "status", "headers")
            .containsExactly("E_EXPIRED_TOKEN", HttpStatus.UNAUTHORIZED, HEADERS);
    }

    @Test
    public void shouldFailWithInvalidJWTError(){
        // stubbing
        when(context.getHeaderString(anyString())).thenReturn(SOME_JWT);
        when(tokenService.extractUser(anyString())).thenThrow(new MalformedJwtException("foo"));

        // execution
        assertThatThrownBy(() -> authenticationFilter.filter(context))
            // assertions
            .isInstanceOf(SecurityException.class)
            .hasNoCause()
            .hasMessage("foo")
            .extracting("code", "status", "headers")
            .containsExactly("E_INVALID_TOKEN", HttpStatus.UNAUTHORIZED, HEADERS);
    }

    @Test
    public void shouldAuthenticateJWT() throws Exception {
        // init
        UserTokenDto userFromToken = new UserTokenDto();
        userFromToken.setRole(Role.SIMPLE);
        userFromToken.setName("foo");
        userFromToken.setId(1L);

        // stubbing
        when(context.getHeaderString(anyString())).thenReturn(SOME_JWT);
        when(tokenService.extractUser(anyString())).thenReturn(userFromToken);

        // execution
        authenticationFilter.filter(context);

        ArgumentCaptor<UserContext> capturedUserContext = ArgumentCaptor.forClass(UserContext.class);
        verify(context).setSecurityContext(capturedUserContext.capture());

        UserContext userContext = capturedUserContext.getValue();

        assertThat(userContext).isNotNull();

        CurrentUser currentUser = userContext.getUser();

        assertThat(userContext.getAuthenticationScheme()).isEqualTo(securityProps.getAuthScheme());
        assertThat(currentUser).isNotNull();
        assertThat(currentUser).isEqualTo(userContext.getUserPrincipal());

        assertThat(currentUser.getRole()).isEqualByComparingTo(userFromToken.getRole());
        assertThat(currentUser.getName()).isEqualTo(userFromToken.getName());
        assertThat(currentUser.getId()).isEqualTo(userFromToken.getId());
    }

    private void failWithMissingJWT(String header){
        // stubbing
        when(context.getHeaderString(anyString())).thenReturn(header);

        // execution
        assertThatThrownBy(() -> authenticationFilter.filter(context))
            // assertions
            .isInstanceOf(SecurityException.class)
            .hasNoCause()
            .hasMessage("JWT is missing")
            .extracting("code", "status", "headers")
            .containsExactly("E_MISSING_TOKEN", HttpStatus.UNAUTHORIZED, HEADERS);
    }
}