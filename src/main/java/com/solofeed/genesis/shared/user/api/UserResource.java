package com.solofeed.genesis.shared.user.api;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Secured
@Component
@Path("/users")
public class UserResource {

    private final IUserService userService;
    private final Validator validator;

    @Context
    private SecurityContext context;

    @GET
    @Path("me")
    public UserDto getUser() throws APIException{
        return userService.getUser();
    }

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }
}
