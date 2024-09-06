package com.example.kanban.mapper;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task taskCreateRequestToTask(TaskCreateRequest request);
}
