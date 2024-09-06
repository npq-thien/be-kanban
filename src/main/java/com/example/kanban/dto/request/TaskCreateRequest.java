package com.example.kanban.dto.request;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class TaskCreateRequest {
    private String name;
    private String description;
    private Instant dateTimeFinish;
    private boolean isPublic;

    @NotNull(message = "Task status cannot be null")
    private TaskStatus status;

    @Nullable
    private TaskPriority priority;
}
