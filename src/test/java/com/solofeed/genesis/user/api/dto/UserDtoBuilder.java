package com.solofeed.genesis.user.api.dto;

import com.solofeed.genesis.user.domain.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data builder for {@link UserDto}
 */
public class UserDtoBuilder {

    private UserDto userDto;


    private UserDtoBuilder(){
        userDto = new UserDto();
    }

    public static UserDtoBuilder aUserDto(){
        return new UserDtoBuilder();
    }

    public static UserDtoBuilder aDefaultUserDto(){
        UserDtoBuilder builder = new UserDtoBuilder();
        return builder.id(1L).name("foo").password("bar").role(Role.SIMPLE).state("foobar")
            .email("foo@bar.com").createdAt(LocalDate.now()).updatedAt(LocalDateTime.now());
    }

    public UserDto build(){
        return userDto;
    }

    public UserDtoBuilder id(Long id){
        userDto.setId(id);
        return this;
    }

    public UserDtoBuilder name(String name){
        userDto.setName(name);
        return this;
    }

    public UserDtoBuilder role(Role role){
        userDto.setRole(role);
        return this;
    }

    public UserDtoBuilder state(String state){
        userDto.setState(state);
        return this;
    }

    public UserDtoBuilder email(String email){
        userDto.setEmail(email);
        return this;
    }

    public UserDtoBuilder password(String password){
        userDto.setPassword(password);
        return this;
    }

    public UserDtoBuilder updatedAt(LocalDateTime update){
        userDto.setUpdatedAt(update);
        return this;
    }

    public UserDtoBuilder createdAt(LocalDate create){
        userDto.setCreatedAt(create);
        return this;
    }
}
