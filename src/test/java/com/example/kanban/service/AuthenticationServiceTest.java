package com.example.kanban.service;

import com.example.kanban.dto.request.AuthenticationRequest;
import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.response.AuthenticationResponse;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.UserRole;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.repository.UserRepository;
import com.example.kanban.service.serviceImpl.AuthenticationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationServiceTest {
    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Mock
    UserRepository userRepository;


    private AuthenticationRequest authenticationRequest;
    private User user;

    // This is just the fake key used for testing
    private static final String TEST_SIGNER_KEY = "GkPOFImiWfyvW+5qP67ZK+u/VXhHniNj+e4CM+FareDHAmUdHtptq/r2HYXGTuyG";

    @BeforeEach
    void initData() {
        // Set the SIGNER_KEY for tests
        ReflectionTestUtils.setField(authenticationService, "SIGNER_KEY", TEST_SIGNER_KEY);

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("admin");
        authenticationRequest.setPassword("1234");

        String encodedPassword = new BCryptPasswordEncoder(10).encode("1234");

        user = User.builder()
                .username("admin")
                .displayName("Admin")
                .password(encodedPassword)
                .role(UserRole.ADMIN)
                .build();
    }

    @Test
    void authenticate_validUser_success() {
        // Mock repository and token generation
        Mockito.when(userRepository.findByUsername(authenticationRequest.getUsername())).thenReturn(Optional.of(user));

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        // Assertions to verify token is generated (not null or empty)
        Assertions.assertThat(response.getToken()).isNotNull();
        Assertions.assertThat(response.getToken()).isNotEmpty();

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(authenticationRequest.getUsername());
    }

    @Test
    void authenticate_wrongPassword_fail() {
        authenticationRequest.setPassword("wrongPassword");
        Mockito.when(userRepository.findByUsername(authenticationRequest.getUsername())).thenReturn(Optional.of(user));

        var exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
                () -> authenticationService.authenticate(authenticationRequest));

        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.UNAUTHENTICATED);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(authenticationRequest.getUsername());
    }
}
