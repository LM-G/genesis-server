package com.solofeed.genesis.user.mapper;

import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.api.dto.CreateUserDtoBuilder;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.api.dto.UserDtoBuilder;
import com.solofeed.genesis.user.domain.Role;
import com.solofeed.genesis.user.domain.UserBuilder;
import com.solofeed.genesis.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link UserMapper}
 */
@RunWith(JUnit4.class)
public class UserMapperTest {

    private UserMapper userMapper;

    @Before
    public void setUp(){
        userMapper = new UserMapperImpl();
    }

    @Test
    public void shouldMapEntityToDto(){
        // init
        User user = UserBuilder.aDefaultUser().build();

        // execution
        UserDto userDto = userMapper.toDto(user);

        // asstions
        assertThat(userDto).isNotNull();
        compare(userDto, user);
    }

    @Test
    public void shouldMapEntitiesToDtos(){
        // init
        List<User> users = Collections.singletonList(UserBuilder.aDefaultUser().build());

        // execution
        List<UserDto> usersDtos = userMapper.toDto(users);

        // asstions
        assertThat(usersDtos).hasSize(1);
        compare(usersDtos.get(0), users.get(0));
    }

    @Test
    public void shouldMapCreationDtoToEntity(){
        // init
        CreateUserDto form = CreateUserDtoBuilder.aDefaultCreateUserDto().build();

        // executiob
        User user = userMapper.fromCreateDto(form);

        // assertions
        assertThat(user).isNotNull();
        assertThat(user.getPassword()).isEqualTo("bar");
        assertThat(user.getEmail()).isEqualTo("foo@bar.com");
        assertThat(user.getName()).isEqualTo("foo");
    }

    @Test
    public void shouldMapDtoToEntity(){
        // init
        UserDto userDto = UserDtoBuilder.aDefaultUserDto().build();

        // execution
        User user = userMapper.fromDto(userDto);

        // asstions
        assertThat(user).isNotNull();
        compare(userDto, user);
    }

    @Test
    public void shouldMapDtosToEntities(){
        // init
        List<UserDto> userDtos = Collections.singletonList(UserDtoBuilder.aDefaultUserDto().build());

        // execution
        List<User> users = userMapper.fromDto(userDtos);

        // asstions
        assertThat(users).hasSize(1);
        compare(userDtos.get(0), users.get(0));
    }

    @Test
    public void shouldMap(){
        // init
        UserDto userDto = UserDtoBuilder.aDefaultUserDto().build();

        // execution
        UserTokenDto userTokenDto = userMapper.toSubjectDto(userDto);

        // assertions
        assertThat(userTokenDto).isNotNull();
        assertThat(userTokenDto.getId()).isEqualTo(1L);
        assertThat(userTokenDto.getRole()).isEqualByComparingTo(Role.SIMPLE);
        assertThat(userTokenDto.getName()).isEqualTo("foo");
    }

    public void compare(UserDto userDto, User user) {
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getState()).isEqualTo(user.getState());
        assertThat(userDto.getRole()).isEqualTo(user.getRole());
        assertThat(userDto.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(userDto.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }
}
