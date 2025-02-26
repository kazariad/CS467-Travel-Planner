package edu.oregonstate.cs467.travelplanner.util.security;

import edu.oregonstate.cs467.travelplanner.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthenticatedUserProvider {
    public User getUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext == null ? null : securityContext.getAuthentication();
        if (authentication == null) return null;
        if (authentication.getPrincipal() instanceof User user) return user;
        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext == null ? null : securityContext.getAuthentication();
        if (authentication == null) return null;
        return authentication.getAuthorities();
    }

    public boolean isUserWithId(long userId) {
        User user = getUser();
        return user != null && user.getUserId() == userId;
    }

    public boolean isAnyUser() {
        return getUser() != null;
    }
}
