package com.solofeed.genesis.user.api;

import com.solofeed.genesis.core.exception.model.APIException;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.domain.User;
import com.solofeed.genesis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.List;

/**
 * Rest resource for {@link User} related stuff handling
 */
@Controller
@Path("/users")
@RequiredArgsConstructor
public class UserResource {

    /** users service */
    private final UserService userService;

    /** Current user context */
    @Context private CurrentUser currentUser;

    /**
     * Get the connected user information
     * @return connected user information
     * @throws APIException if user not found
     */
    @GET
    @Secured
    @Path("/me")
    public UserDto getUser() throws APIException{
        Long userId = currentUser.getId();
        return userService.getUser(userId);
    }

    /**
     * Get the list of connected users
     * @return list of connected users
     */
    @GET
    @Path("/connected")
    public List<UserDto> getConnectedUsers(){
        return userService.getConnectedUsers();
    }
}
