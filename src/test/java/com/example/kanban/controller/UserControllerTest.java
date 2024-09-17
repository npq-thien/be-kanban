package com.example.kanban.controller;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;

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
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        //    GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userCreateRequest);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);
        //    WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("User created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.id")
                        .value("aabb1122"))
                .andExpect((MockMvcResultMatchers.jsonPath("data.role")
                        .value("MEMBER")));
    }

    @Test
    void createUser_passwordLessThan4Characters_fail() throws Exception {
        //    GIVEN
        userCreateRequest.setPassword("123");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(userCreateRequest);

        //    WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Validation error"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.password")
                        .value("Password must contain more than 4 characters"));
    }
}
