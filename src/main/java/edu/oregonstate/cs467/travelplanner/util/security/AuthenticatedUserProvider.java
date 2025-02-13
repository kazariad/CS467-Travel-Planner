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
        Authentication authentication = getAuthentication();
        if (authentication == null) return null;
        if (authentication.getPrincipal() instanceof User user) return user;
        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication authentication = getAuthentication();
        if (authentication == null) return null;
        return authentication.getAuthorities();
    }

    public boolean checkUser(long userId) {
        User user = getUser();
        return user != null && user.getUserId() == userId;
    }

    Authentication getAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext == null ? null : securityContext.getAuthentication();
    }
}
