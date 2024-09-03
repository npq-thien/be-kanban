package com.example.kanban.service;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.entity.User;

public interface UserService {
    User createUser(UserCreateRequest request);
}
