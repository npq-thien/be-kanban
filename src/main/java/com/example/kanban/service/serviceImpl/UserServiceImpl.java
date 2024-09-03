package com.example.kanban.service.serviceImpl;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.entity.User;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.mapper.UserMapper;
import com.example.kanban.repository.UserRepository;
import com.example.kanban.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists", ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.userCreateRequestToUser(request);
        userRepository.save(user);

        return user;
    }
}
