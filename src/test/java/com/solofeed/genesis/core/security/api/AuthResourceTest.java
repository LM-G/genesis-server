package com.solofeed.genesis.core.security.api;

import com.solofeed.genesis.core.exception.model.FunctionalException;
import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.security.api.dto.SignInDto;
import com.solofeed.genesis.core.security.api.dto.TokensDto;
import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.core.security.service.TokenService;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.service.IUserService;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test {@link AuthResource}
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthResourceTest {
    private AuthResource authResource;

    @Mock
    private IUserService userService;
    @Mock
    private TokenService tokenService;

    @Before
    public void setUp() throws Exception {
        authResource = new AuthResource(userService, tokenService);
    }

    @Test
    public void shouldNotLogInBecauseUnknownUser() throws Exception {
        // init
        SignInDto signInDto = new SignInDto();
        signInDto.setLogin("foo");
        signInDto.setPassword("bar");
        FunctionalException notFoundException = new FunctionalException(HttpStatus.NOT_FOUND, "E_USER_NOT_FOUND", "user not found");

        // stubbing
        doThrow(notFoundException).when(userService).getUser("foo", "bar");

        // execution
        assertThatThrownBy(() -> authResource.login(signInDto))
                // assertions
                .isInstanceOf(FunctionalException.class)
                .hasNoCause()
                .hasMessage("user not found")
                .extracting("status","code")
                .containsExactly(HttpStatus.NOT_FOUND, "E_USER_NOT_FOUND");
    }

    @Test
    public void shouldNotLogInBecauseWrongCredentials() throws Exception {
        // init
        SignInDto signInDto = new SignInDto();
        signInDto.setLogin("foo");
        signInDto.setPassword("bar");
        SecurityException wrongCredentialsException = new SecurityException("E_WRONG_CREDENTIALS", "Authentication failed, wrong credentials");

        // stubbing
        doThrow(wrongCredentialsException).when(userService).getUser("foo", "bar");

        // execution
        assertThatThrownBy(() -> authResource.login(signInDto))
                // assertions
                .isInstanceOf(SecurityException.class)
                .hasNoCause()
                .hasMessage("Authentication failed, wrong credentials")
                .extracting("status","code")
                .containsExactly(HttpStatus.UNAUTHORIZED, "E_WRONG_CREDENTIALS");
    }

    @Test
    public void shouldLogIn() throws Exception {
        // init
        SignInDto signInDto = new SignInDto();
        signInDto.setLogin("foo");
        signInDto.setPassword("bar");

        TokensDto tokens = new TokensDto("barfoo", "foobar");

        // stubbing
        when(userService.getUser("foo", "bar")).thenReturn(new UserDto());
        when(tokenService.createTokens(any(UserDto.class))).thenReturn(tokens);

        // execution
        TokensDto tokensDto = authResource.login(signInDto);

        // assertions
        assertThat(tokensDto).isNotNull();
        assertThat(tokensDto.getAccessToken()).isEqualTo("barfoo");
        assertThat(tokensDto.getRefreshToken()).isEqualTo("foobar");
    }

    @Test
    public void shouldRefreshJWT() throws Exception {
        // init
        UserTokenDto userTokenDto = new UserTokenDto();
        userTokenDto.setId(1L);
        UserDto userDto = new UserDto();
        userDto.setState("bar");

        // stubbing
        when(tokenService.extractUser("foo")).thenReturn(userTokenDto);
        when(tokenService.getIdentifier("foo")).thenReturn("bar");
        when(userService.getUser(1L)).thenReturn(userDto);
        when(tokenService.refresh("foo")).thenReturn(new TokensDto("foobar", "barfoo"));

        // execution
        TokensDto tokensDto = authResource.refresh("foo");

        // assertions
        assertThat(tokensDto).isNotNull();
        assertThat(tokensDto.getAccessToken()).isEqualTo("foobar");
        assertThat(tokensDto.getRefreshToken()).isEqualTo("barfoo");
    }

    @Test
    public void shouldNotRefreshJWTBecauseItIsOutOfDate() throws Exception {
        // init
        UserTokenDto userTokenDto = new UserTokenDto();
        userTokenDto.setId(1L);
        UserDto userDto = new UserDto();
        userDto.setState("foo");

        // stubbing
        when(tokenService.extractUser("foo")).thenReturn(userTokenDto);
        when(tokenService.getIdentifier("foo")).thenReturn("bar");
        when(userService.getUser(1L)).thenReturn(userDto);

        // execution
        assertThatThrownBy(() -> authResource.refresh("foo"))
                // assertions
                .isInstanceOf(SecurityException.class)
                .hasMessage("User identity was updated, token out of date")
                .hasNoCause()
                .extracting("status", "code")
                .containsExactly(HttpStatus.UNAUTHORIZED, "E_OUT_OF_DATE_TOKEN");
    }

    @Test
    public void shouldRegister() throws Exception {
        // init
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("foo@bar.com");
        createUserDto.setName("foo");
        createUserDto.setPassword("bar");

        // stubbing


        // execution
        authResource.register(createUserDto);

        ArgumentCaptor<CreateUserDto> createUserDtoCaptor = ArgumentCaptor.forClass(CreateUserDto.class);
        verify(userService).createUser(createUserDtoCaptor.capture());

        assertThat(createUserDtoCaptor.getValue()).isNotNull();
        assertThat(createUserDtoCaptor.getValue().getEmail()).isEqualTo("foo@bar.com");
        assertThat(createUserDtoCaptor.getValue().getName()).isEqualTo("foo");
        assertThat(createUserDtoCaptor.getValue().getPassword()).isEqualTo("bar");
    }

    @Test
    public void logout() throws Exception {
        assertThatThrownBy(() -> authResource.logout()).isInstanceOf(NotImplementedException.class);
    }

}