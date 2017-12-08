package com.solofeed.genesis.user.service;

import com.solofeed.genesis.IntegrationTest;
import com.solofeed.genesis.core.exception.model.APIException;
import com.solofeed.genesis.core.exception.model.FunctionalException;
import com.solofeed.genesis.core.security.service.PasswordEncoder;
import com.solofeed.genesis.core.AppState;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.api.dto.CreateUserDtoBuilder;
import com.solofeed.genesis.user.dao.UserRepository;
import com.solofeed.genesis.user.domain.Role;
import com.solofeed.genesis.user.domain.User;
import com.solofeed.genesis.user.mapper.UserMapper;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceIntegrationTest extends IntegrationTest{
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private AppState appState;

    @Before
    public  void setUp(){
        userService = new UserService(userRepository, userMapper, passwordEncoder, appState);
    }

    @Test
    public void _1_shouldCreateUser() throws APIException {
        // init
        CreateUserDto form = CreateUserDtoBuilder.aDefaultCreateUserDto().build();

        // execution
        userService.createUser(form);

        User user = userRepository.findByName("foo");
        boolean match = passwordEncoder.matches("bar", user.getPassword());

        // assertions
        assertThat(user.getEmail()).isEqualTo("foo@bar.com");
        assertThat(match).isTrue();
        assertThat(user.getRole()).isEqualByComparingTo(Role.SIMPLE);
        assertThat(user.getState()).isNotBlank();
        assertThat(user.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(user.getUpdatedAt()).isCloseTo(LocalDateTime.now(), new TemporalUnitLessThanOffset(2000L, ChronoUnit.MILLIS));
    }

    @Test
    public void _2_shouldNotCreateUserBecauseConflict() throws APIException {
        // init
        CreateUserDto form = CreateUserDtoBuilder.aDefaultCreateUserDto().build();

        // execution
        assertThatThrownBy(() -> userService.createUser(form))
            .isInstanceOf(FunctionalException.class)
            .hasNoCause()
            .hasMessage("User registration failed")
            .extracting("status", "code")
            .containsExactly(HttpStatus.CONFLICT, "E_USER_REGISTRATION_FAILED");
    }
}
