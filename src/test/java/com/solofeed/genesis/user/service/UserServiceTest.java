package com.solofeed.genesis.user.service;

import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.exception.model.FunctionalException;
import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.security.service.PasswordEncoder;
import com.solofeed.genesis.core.AppState;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.api.dto.UserDtoBuilder;
import com.solofeed.genesis.user.dao.UserRepository;
import com.solofeed.genesis.user.domain.User;
import com.solofeed.genesis.user.domain.UserBuilder;
import com.solofeed.genesis.user.mapper.UserMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppState state;

    @Before
    public void setUp(){
        userService = new UserService(userRepository, userMapper, passwordEncoder, state);
    }


    @Test
    public void shouldGetUserFromId() throws Exception {
        // init
        User user = new User();

        // stubbing
        when(userRepository.findOne(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDto());

        // execution
        UserDto userDto = userService.getUser(1L);

        // assertions
        assertThat(userDto).isNotNull();
    }

    @Test
    public void shouldNotGetUserFromIdBecauseUserNotFound() throws Exception {
        // stubbing
        when(userRepository.findOne(1L)).thenReturn(null);

        // execution
        assertThatThrownBy(() -> userService.getUser(1L))
            // assertions
            .isInstanceOf(FunctionalException.class)
            .hasNoCause()
            .hasMessage("User not found")
            .extracting("status", "code")
            .containsExactly(HttpStatus.NOT_FOUND, "E_USER_NOT_FOUND");
    }

    @Test
    public void shouldGetUserFromLoginPassword() throws Exception {
        // init
        User user = new User();
        UserDto userDto = UserDtoBuilder.aUserDto().password("hashedBar").build();
        // stubbing
        when(userRepository.findByNameOrEmail("foo", "foo")).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(passwordEncoder.matches("bar", "hashedBar")).thenReturn(true);

        // execution
        UserDto result = userService.getUser("foo", "bar");

        // assertions
        assertThat(result).isNotNull();
    }

    @Test
    public void shouldNotGetUserFromLoginPasswordBecauseUserNotFound() throws Exception {
        // stubbing
        when(userRepository.findByNameOrEmail("foo", "foo")).thenReturn(null);

        // execution
        assertThatThrownBy(() -> userService.getUser("foo", "bar"))
            // assertions
            .isInstanceOf(FunctionalException.class)
            .hasNoCause()
            .hasMessage("User not found")
            .extracting("status", "code")
            .containsExactly(HttpStatus.NOT_FOUND, "E_USER_NOT_FOUND");
    }

    @Test
    public void shouldNotGetUserFromLoginPasswordBecauseWrongCredentials() throws Exception {
        // init
        User user = new User();
        UserDto userDto = UserDtoBuilder.aUserDto().password("hashedBar").build();
        // stubbing
        when(userRepository.findByNameOrEmail("foo", "foo")).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(passwordEncoder.matches("bar", "hashedBar")).thenReturn(false);

        // execution
        assertThatThrownBy(() -> userService.getUser("foo", "bar"))
            // assertions
            .isInstanceOf(SecurityException.class)
            .hasNoCause()
            .hasMessage("Authentication failed, wrong credentials")
            .extracting("status", "code")
            .containsExactly(HttpStatus.UNAUTHORIZED, "E_WRONG_CREDENTIALS");
    }

    @Test
    public void shouldNotCreateUserBecauseUserAlreadyExists() throws Exception {
        // init
        CreateUserDto form = new CreateUserDto();
        form.setName("foo");
        form.setEmail("foo@bar.com");

        User user = UserBuilder.aUser().name("foo").email("foo@bar.com").build();

        Map<String, String> detail = ImmutableMap.of("email","false","name","false");

        // stubbing
        when(userRepository.findByNameOrEmail("foo", "foo@bar.com")).thenReturn(user);

        // execution
        assertThatThrownBy(() -> userService.createUser(form))
            // assertions
            .isInstanceOf(FunctionalException.class)
            .hasNoCause()
            .hasMessage("User registration failed")
            .extracting("status", "code", "detail")
            .containsExactly(HttpStatus.CONFLICT, "E_USER_REGISTRATION_FAILED", detail);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        // init
        CreateUserDto form = new CreateUserDto();
        form.setName("foo");
        form.setEmail("foo@bar.com");

        User user = UserBuilder.aUser().password("bar").build();
        UserDto userDto = UserDtoBuilder.aUserDto().name("foo").email("foo@bar.com").build();

        // stubbing
        when(userRepository.findByNameOrEmail("foo", "foo@bar.com")).thenReturn(null);
        when(userMapper.fromCreateDto(form)).thenReturn(user);
        when(passwordEncoder.encode("bar")).thenReturn("hashedbar");

        // execution
        userService.createUser(form);

        // assertion
        verify(userRepository).save(user);

        assertThat(user.getPassword()).isEqualTo("hashedbar");
    }

}