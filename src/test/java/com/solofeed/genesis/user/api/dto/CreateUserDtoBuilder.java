package com.solofeed.genesis.user.api.dto;

/**
 * Data builder for {@link CreateUserDto}
 */
public class CreateUserDtoBuilder {

    private CreateUserDto createUserDto;


    private CreateUserDtoBuilder(){
        createUserDto = new CreateUserDto();
    }

    public static CreateUserDtoBuilder aCreateUserDto(){
        return new CreateUserDtoBuilder();
    }

    public static CreateUserDtoBuilder aDefaultCreateUserDto(){
        CreateUserDtoBuilder builder = new CreateUserDtoBuilder();
        return builder.name("foo").password("bar").email("foo@bar.com");
    }

    public CreateUserDto build(){
        return createUserDto;
    }

    public CreateUserDtoBuilder name(String name){
        createUserDto.setName(name);
        return this;
    }

    public CreateUserDtoBuilder email(String email){
        createUserDto.setEmail(email);
        return this;
    }

    public CreateUserDtoBuilder password(String password){
        createUserDto.setPassword(password);
        return this;
    }
}
