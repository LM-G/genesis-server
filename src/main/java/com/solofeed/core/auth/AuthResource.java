package com.solofeed.core.auth;

import com.solofeed.core.exception.APIException;
import com.solofeed.shared.user.dto.UserDto;
import com.solofeed.shared.user.dto.CreateUserDto;
import com.solofeed.core.auth.dto.SignInDto;
import com.solofeed.shared.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    /** User endpoint logger */
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

    @Inject
    private IUserService userService;

    @Inject
    private AuthService authService;

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @POST
    @Path("/sign-in")
    public UserDto login(@Valid SignInDto form) throws APIException {
        // gets the user
        UserDto user = userService.getUser(form.getLogin(), form.getPassword());
        String token = authService.createToken(user);
        return user;
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
