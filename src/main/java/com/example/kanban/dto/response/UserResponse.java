package com.example.kanban.dto.response;

import com.example.kanban.entity.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String password;
    private String displayName;
    private String role;
}
