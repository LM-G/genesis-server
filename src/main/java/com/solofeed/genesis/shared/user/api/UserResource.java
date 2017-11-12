package com.solofeed.genesis.shared.user.api;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.List;

@RequiredArgsConstructor
@Secured @Component
@Path("/users")
public class UserResource {

    /** users service */
    private final IUserService userService;

    /** Current user context */
    @Context private CurrentUser currentUser;

    @GET
    @Path("me")
    public UserDto getUser() throws APIException{
        Long userId = currentUser.getId();
        return userService.getUser(userId);
    }

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }
}
