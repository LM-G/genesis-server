package com.solofeed.genesis.core.security.api.filter;

import com.solofeed.genesis.shared.user.api.dto.UserDto;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by LM-G on 09/11/2017.
 */
@Provider
@PreMatching
public class CurrentUserFilter implements ContainerRequestFilter{

    public static final String USER_PROPERTY = "USER";

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("test");
        context.setProperty(USER_PROPERTY, userDto);
    }
}
