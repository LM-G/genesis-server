package com.solofeed.genesis.core.auth;

import com.solofeed.genesis.core.auth.dto.TokenDto;
import com.solofeed.genesis.core.auth.service.CypherService;
import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.shared.user.dto.UserDto;
import com.solofeed.genesis.shared.user.dto.CreateUserDto;
import com.solofeed.genesis.core.auth.dto.SignInDto;
import com.solofeed.genesis.shared.user.service.IUserService;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by LM-G on 25/09/2017.
 */
@Log4j2
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private IUserService userService;
    @Inject
    private CypherService cypherService;

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @POST
    @Path("/sign-in")
    public TokenDto login(@Valid SignInDto form) throws APIException {
        // gets the user
        UserDto user = userService.getUser(form.getLogin(), form.getPassword());

        // generate JWT token
        String token = cypherService.createToken(user);

        return new TokenDto(token);
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
