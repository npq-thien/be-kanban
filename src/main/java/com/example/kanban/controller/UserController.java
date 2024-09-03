package com.example.kanban.controller;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.entity.User;
import com.example.kanban.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping()
    ResponseEntity<ApiResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        User user = userService.createUser(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("User created successfully")
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
