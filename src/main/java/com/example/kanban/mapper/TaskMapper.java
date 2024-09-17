package com.example.kanban.mapper;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task taskCreateRequestToTask(TaskCreateRequest request);

    @Mapping(source = "assignedUser.id", target = "assignedUserId")
    @Mapping(source = "assignedUser.displayName", target = "assignedUserDisplayName")
    TaskResponse taskToTaskResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdByUsername", ignore = true)
    void updateTaskFromRequest(TaskUpdateRequest request, @MappingTarget Task task);
}
