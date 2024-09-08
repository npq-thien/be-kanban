package com.example.kanban.config.security;

import com.example.kanban.service.AuthenticationService;
import com.example.kanban.service.serviceImpl.AuthenticationServiceImpl;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final CustomUserDetailsService customUserDetailsService; // Use CustomUserDetailsService

    public JwtFilter(AuthenticationServiceImpl authenticationService, CustomUserDetailsService customUserDetailsService) {
        this.authenticationService = authenticationService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = null;
        String token = null;
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                username = signedJWT.getJWTClaimsSet().getSubject();
            } catch (Exception e) {
                logger.error("JWT Token parsing failed", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            This is AuthUser
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            try {
                if (authenticationService.introspect(token)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                logger.error("Token introspection failed", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}