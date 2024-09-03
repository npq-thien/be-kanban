package com.example.kanban.mapper;

import com.example.kanban.dto.request.UserCreateRequest;
import com.example.kanban.dto.response.UserResponse;
import com.example.kanban.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userCreateRequestToUser(UserCreateRequest request);
    UserResponse usertoUserResponse(User user);

}
