package com.solofeed.genesis.core.security.api.filter;

import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.core.security.domain.UserContext;
import com.solofeed.genesis.user.domain.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationFilterTest {

    private AuthorizationFilter authorizationFilter;
    private UserContext userContext;

    @Mock
    private ResourceInfo resourceInfo;

    @Mock
    private ContainerRequestContext context;

    @Before
    public void setUp() {
        // filter creation
        authorizationFilter = new AuthorizationFilter();
        CurrentUser user = new CurrentUser();
        user.setRole(Role.SIMPLE);
        userContext = new UserContext(user, "Bearer");


        ReflectionTestUtils.setField(authorizationFilter, "resourceInfo", resourceInfo);
    }

    @Test
    public void shouldAuthorizeSimpleActionForNonSecuredResource() throws Exception {
        // stubbing
        when(context.getSecurityContext()).thenReturn(userContext);
        doReturn(Dummy.class).when(resourceInfo).getResourceClass();
        doReturn(Dummy.class.getMethod("simpleRestricted")).when(resourceInfo).getResourceMethod();

        // execution
        authorizationFilter.filter(context);
    }

    @Test
    public void shouldNotAuthorizeAdminSecuredActionForNonSecuredResourceIfUserSimple() throws Exception {
        // stubbing
        when(context.getSecurityContext()).thenReturn(userContext);
        doReturn(Dummy.class).when(resourceInfo).getResourceClass();
        doReturn(Dummy.class.getMethod("adminRestricted")).when(resourceInfo).getResourceMethod();

        // execution
        assertThatThrownBy(() -> authorizationFilter.filter(context))
            // assertions
            .isInstanceOf(SecurityException.class)
            .hasNoCause()
            .hasMessage("Insufficient permission, access denied")
            .extracting("status", "code")
            .containsExactly(HttpStatus.FORBIDDEN, "E_INSUFFICIENT_PERMISSION");
    }

    @Test
    public void shouldNotAuthorizeActionForSecuredResource() throws Exception {
        // stubbing
        when(context.getSecurityContext()).thenReturn(userContext);
        doReturn(SecuredDummy.class).when(resourceInfo).getResourceClass();
        doReturn(SecuredDummy.class.getMethod("adminRestricted")).when(resourceInfo).getResourceMethod();

        // execution
        assertThatThrownBy(() -> authorizationFilter.filter(context))
            // assertions
            .isInstanceOf(SecurityException.class)
            .hasNoCause()
            .hasMessage("Insufficient permission, access denied")
            .extracting("status", "code")
            .containsExactly(HttpStatus.FORBIDDEN, "E_INSUFFICIENT_PERMISSION");
    }

    @Test
    public void shouldAuthorizeLessSecuredActionForSecuredResourceIfSufficientPermission() throws Exception {
        // stubbing
        when(context.getSecurityContext()).thenReturn(userContext);
        doReturn(SecuredDummy.class).when(resourceInfo).getResourceClass();
        doReturn(SecuredDummy.class.getMethod("simpleRestricted")).when(resourceInfo).getResourceMethod();

        // execution
        authorizationFilter.filter(context);
    }


    public static class Dummy {
        @Secured
        public void simpleRestricted(){}
        @Secured(Role.ADMIN)
        public void adminRestricted(){}
    }

    @Secured(Role.ADMIN)
    public static class SecuredDummy {
        public void adminRestricted(){}
        @Secured
        public void simpleRestricted(){}
    }
}