package com.example.kanban.dto.request;

import com.example.kanban.entity.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, message = "Username must contain more than 3 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 4, message = "Password must contain more than 4 characters")
    private String password;

    @NotNull(message = "Display name cannot be null")
    private String displayName;

    private UserRole role = UserRole.MEMBER;
}
