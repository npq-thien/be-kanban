package com.example.kanban.dto.response;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String id;
    private String name;
    private String description;
    private Instant dateTimeStart;
    private Instant dateTimeFinish;
    private Boolean isPublic;
    private String creatorDisplayName;
    private String createdByUsername;
    private TaskStatus status;
    private int position;
    private TaskPriority priority;
    private String assignedUserId;
    private String assignedUserDisplayName;

    // List id of images belongs to task
    private List<String> imageIds;
}
