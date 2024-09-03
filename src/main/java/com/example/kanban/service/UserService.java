package com.example.kanban.service;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.request.UserUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    ApiResponse getAllUsers();
    UserResponse getUser(String id);
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
}
