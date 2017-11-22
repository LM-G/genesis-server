package com.solofeed.genesis.core.provider;

import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.user.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.SecurityContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test {@link CurrentUserProvider}
 */
@RunWith(MockitoJUnitRunner.class)
public class CurrentUserProviderTest {
    @InjectMocks
    private CurrentUserProvider currentUserProvider;

    @Mock
    private SecurityContext securityContext;

    @Test
    public void shouldProvideCurrentUser(){
        // init test input
        CurrentUser expectedUser = new CurrentUser(1L, "foo", Role.SIMPLE);

        // stubbing
        when(securityContext.getUserPrincipal()).thenReturn(expectedUser);

        // execution
        CurrentUser user = currentUserProvider.provide();

        // assertions
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(expectedUser.getId());
        assertThat(user.getName()).isEqualTo(expectedUser.getName());
        assertThat(user.getRole()).isEqualByComparingTo(expectedUser.getRole());
    }

    @Test
    public void shouldDoNothingOnDispose(){
        // init test input
        CurrentUser user = new CurrentUser(1L, "foo", Role.SIMPLE);

        // execution
        currentUserProvider.dispose(user);
    }
}
