package com.example.kanban.config;


import com.example.kanban.config.security.AuthUser;
import com.example.kanban.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.text.ParseException;
import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<AuthUser> {
    @Autowired
    AuthenticationService authenticationService;

    @Override
    public Optional<AuthUser> getCurrentAuditor() {
        // Get the current user's username from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        // Ensure principal is an instance of AuthUser and cast it then return displayName
        if (principal instanceof UserDetails) {
            AuthUser authUser = (AuthUser) principal;
            return Optional.of(authUser);
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.of(new AuthUser());
    }

    //public Optional<String> getCurrentUsername() {
    //    // Get the current user's username from SecurityContext
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    Object principal = authentication.getPrincipal();
    //
    //    // Ensure principal is an instance of AuthUser and cast it then return displayName
    //    if (principal instanceof UserDetails) {
    //        AuthUser authUser = (AuthUser) principal;
    //        return Optional.of(authUser.getUsername());
    //    }
    //
    //    if (authentication == null || !authentication.isAuthenticated()) {
    //        return Optional.empty();
    //    }
    //    return Optional.of(authentication.getName());
    //}
}
