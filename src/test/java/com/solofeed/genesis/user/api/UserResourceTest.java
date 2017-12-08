package com.solofeed.genesis.user.api;

import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.api.dto.UserDtoBuilder;
import com.solofeed.genesis.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Test {@link UserResource}
 */
@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    private UserResource userResource;

    @Mock
    private UserService userService;

    @Mock
    private CurrentUser currentUser;

    @Before
    public void setUp(){
        userResource = new UserResource(userService);
        ReflectionTestUtils.setField(userResource, "currentUser", currentUser);
    }

    @Test
    public void shouldGetUser() throws Exception {
        // init
        UserDto userDto = UserDtoBuilder.aDefaultUserDto().build();

        // stubbing
        doReturn(1L).when(currentUser).getId();
        when(userService.getUser(1L)).thenReturn(userDto);

        // execution
        UserDto result = userResource.getUser();

        // assertions
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(userDto);
    }

}