package com.example.believers_network_backend.demo.security;

import com.example.believers_network_backend.demo.error.UnauthenticatedAccessException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
@Order(1)
public class SecurityGraphQLAspect {

    /**
     * All graphQLResolver methods can be called only by authenticated user.
     *
     * @Unsecured annotated methods are excluded author- Otti Daniel
     */

    @Before("allGraphQLResolverMethods() && isDefinedInApplication() && !isMethodAnnotatedAsUnsecured()")
    public void doSecurityCheck() {
        if (SecurityContextHolder.getContext() == null ||
                SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
                AnonymousAuthenticationToken.class.isAssignableFrom(SecurityContextHolder.getContext().getAuthentication().getClass())) {
            throw new UnauthenticatedAccessException("Sorry, you should log in first to do that!");
        }
    }

    /**
     * @AdminSecured annotated methods can be called only by admin authenticated user.
     */
    @Before("isMethodAnnotatedAsAdminUnsecured()")
    public void doAdminSecurityCheck() {
        if (!isAuthorized()) {
            throw new UnauthenticatedAccessException("Sorry, you do not have enough rights to do that!");
        }
    }


    /**
     * Matches all beans that implement {@link com.coxautodev.graphql.tools.GraphQLResolver} as
     * {@code UserMutation}, {@code UserQuery}
     * extend GraphQLResolver interface
     */
    @Pointcut("target(com.coxautodev.graphql.tools.GraphQLResolver)")
    private void allGraphQLResolverMethods() {
        //leave empty
    }

    /**
     * Matches all beans in com.example.believers_network_backend.demo.errorhandling package
     */
    @Pointcut("within(com.example.believers_network_backend.demo..*)")
    private void isDefinedInApplication() {
        //leave empty
    }

    /**
     * Any method annotated with @Unsecured
     */
    @Pointcut("@annotation(com.example.believers_network_backend.demo.security.Unsecured)")
    private void isMethodAnnotatedAsUnsecured() {
        //leave empty
    }

    /**
     * Any method annotated with @AdminSecured
     */
    @Pointcut("@annotation(com.example.believers_network_backend.demo.security.AdminSecured)")
    private void isMethodAnnotatedAsAdminUnsecured() {
        //leave empty
    }

    private boolean isAuthorized() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority auth : authorities) {
                if (auth.getAuthority().equals("ROLE_ADMIN"))
                    return true;
            }
        }
        return false;
    }
}
