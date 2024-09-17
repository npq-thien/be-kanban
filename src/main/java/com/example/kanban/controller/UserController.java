package com.example.kanban.controller;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.request.UserUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    ResponseEntity<ApiResponse> getAllUsers() {
        ApiResponse apiResponse = userService.getAllUsers();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{userId}")
    ResponseEntity<ApiResponse> getUser(@PathVariable String userId) {
        UserResponse userResponse = userService.getUser(userId);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("User retrieved successfully")
                .data(userResponse)
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    ResponseEntity<ApiResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserResponse userResponse = userService.createUser(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("User created successfully")
                .data(userResponse)
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{userId}")
    ResponseEntity<ApiResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUser(userId, request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("User updated successfully")
                .data(userResponse)
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @DeleteMapping("/{userId}")
    ResponseEntity<ApiResponse> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("User deleted successfully")
                .timestamp(new Date())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
