package com.solofeed.genesis.core.security.api;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.api.dto.TokensDto;
import com.solofeed.genesis.core.security.service.TokenFactory;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.api.dto.CreateUserDto;
import com.solofeed.genesis.core.security.api.dto.SignInDto;
import com.solofeed.genesis.shared.user.service.IUserService;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by LM-G on 25/09/2017.
 */
@Log4j2
@Path("/auth")
public class AuthResource {

    @Inject
    private IUserService userService;
    @Inject
    private TokenFactory tokenFactory;

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @POST
    @Path("/sign-in")
    public TokensDto login(@Valid SignInDto form) throws APIException {
        // gets the user
        UserDto user = userService.getUser(form.getLogin(), form.getPassword());

        // generate JWT token
        return tokenFactory.createTokens(user);
    }

    @POST
    @Path("/sign-up")
    public void register(@Valid CreateUserDto form) throws APIException{
        userService.createUser(form);
    }

    @POST
    @Path("/sign-out")
    public void logout(SignInDto form){
    }
}
