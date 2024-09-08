package com.example.kanban.service.serviceImpl;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.request.UserUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.entity.User;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.mapper.UserMapper;
import com.example.kanban.repository.UserRepository;
import com.example.kanban.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists", ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.userCreateRequestToUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return userMapper.usertoUserResponse(user);
    }

    @Override
    public ApiResponse getAllUsers() {
        List<UserResponse> userResponseDTO = userRepository.findAll()
                .stream().map(userMapper::usertoUserResponse).toList();

        // Create a map or a custom response object to include the user count
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("count", userResponseDTO.size());
        responseData.put("users", userResponseDTO);

        return ApiResponse.builder()
                .code(200)
                .message("Users retrieved successfully")
                .data(responseData)
                .timestamp(new Date())
                .build();
    }

    @Override
    public UserResponse getUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found", ErrorCode.USER_NOT_FOUND));

        return userMapper.usertoUserResponse(user);
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found", ErrorCode.USER_NOT_FOUND));

        user.setDisplayName(request.getDisplayName());
        userRepository.save(user);

        return userMapper.usertoUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found", ErrorCode.USER_NOT_FOUND));

        userRepository.deleteById(id);
    }
}
