package com.solofeed.genesis.user.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data builder for {@link User}
 */
public class UserBuilder {

    private User user;


    private UserBuilder(){
        user = new User();
    }

    public static UserBuilder aUser(){
        return new UserBuilder();
    }

    public static UserBuilder aDefaultUser(){
        UserBuilder builder = new UserBuilder();
        return builder.id(1L).name("foo").password("bar").role(Role.SIMPLE).state("foobar")
            .email("foo@bar.com").createdAt(LocalDate.now()).updatedAt(LocalDateTime.now());
    }

    public User build(){
        return user;
    }

    public UserBuilder id(Long id){
        user.setId(id);
        return this;
    }

    public UserBuilder name(String name){
        user.setName(name);
        return this;
    }

    public UserBuilder role(Role role){
        user.setRole(role);
        return this;
    }

    public UserBuilder state(String state){
        user.setState(state);
        return this;
    }

    public UserBuilder email(String email){
        user.setEmail(email);
        return this;
    }

    public UserBuilder password(String password){
        user.setPassword(password);
        return this;
    }

    public UserBuilder updatedAt(LocalDateTime update){
        user.setUpdatedAt(update);
        return this;
    }

    public UserBuilder createdAt(LocalDate create){
        user.setCreatedAt(create);
        return this;
    }
}
