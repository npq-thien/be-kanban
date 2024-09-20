package com.example.kanban.service;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.UserRole;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;

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
                .role(UserRole.MEMBER)
                .build();

        userResponse = UserResponse.builder()
                .username("tester")
                .displayName("Test")
                .role("MEMBER")
                .build();

        user = User.builder()
                .username("tester")
                .displayName("Test")
                .role(UserRole.MEMBER)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(userCreateRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(userCreateRequest);
        log.info(response.toString());
        // THEN
        Assertions.assertThat(response.getUsername()).isEqualTo("tester");
        Assertions.assertThat(response.getDisplayName()).isEqualTo("Test");
        Assertions.assertThat(response.getRole()).isEqualTo("MEMBER");
    }

    @Test
    void createUser_existUsername_fail() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

        var exception = assertThrows(BusinessException.class, () -> userService.createUser(userCreateRequest));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_ALREADY_EXISTS);
    }

}
