package com.example.kanban.dto.request;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {
    private String name;
    private String description;
    private Instant dateTimeFinish;
    private Boolean isPublic;
    private String createdByUsername;

    @NotNull(message = "Task status cannot be null")
    private TaskStatus status;

    @Nullable
    private TaskPriority priority;
}