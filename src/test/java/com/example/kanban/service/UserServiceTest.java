package com.example.kanban.service;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.UserRole;
import com.example.kanban.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;
    private User user;

    @BeforeEach
    void initData() {
        userCreateRequest = UserCreateRequest.builder()
                .username("tester")
                .password("1234")
                .displayName("Test")
                .build();

        userResponse = UserResponse.builder()
                .id("aabb1122")
                .username("tester")
                .displayName("Test")
                .role("MEMBER")
                .build();

        user = User.builder()
                .id("aabb1122")
                .username("tester")
                .displayName("Test")
                .role(UserRole.MEMBER)
                .build();
    }

}
