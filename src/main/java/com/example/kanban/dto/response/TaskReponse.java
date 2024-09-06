package com.example.kanban.dto.response;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

public class TaskReponse {
    private String id;
    private String name;
    private String description;
    private Instant dateTimeStart;
    private Instant dateTimeFinish;
    private boolean isPublic;
    private String creatorDisplayName;
    private TaskStatus status;
    private TaskPriority priority;
}
