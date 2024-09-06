package com.example.kanban.dto.request;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {
    private String name;
    private String description;
    private Instant dateTimeFinish;
    private Boolean isPublic;

    @NotNull(message = "Task status cannot be null")
    private TaskStatus status;

    @Nullable
    private TaskPriority priority;
}
