package com.example.kanban.dto.response;

import com.example.kanban.entity.Comment;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;

import java.time.Instant;
import java.util.List;

public class TaskDetailResponse {
    private String id;
    private String name;
    private String description;
    private Instant dateTimeStart;
    private Instant dateTimeFinish;
    private Boolean isPublic;
    private String creatorDisplayName;
    private String createdByUsername;
    private TaskStatus status;
    private TaskPriority priority;
    private User assignedUser;
    private List<Comment> comments;
}
