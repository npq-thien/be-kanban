package com.example.kanban.config.security;

import com.example.kanban.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser implements UserDetails {
    private String username;
    private String password;
    private String displayName;

    public static AuthUser create(User user) {
        return AuthUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .displayName(user.getDisplayName())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
