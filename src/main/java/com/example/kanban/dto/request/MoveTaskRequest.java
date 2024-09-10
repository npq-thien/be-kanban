package com.example.kanban.dto.request;

import com.example.kanban.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveTaskRequest {
    private String taskId;
    private int startPosition;
    private int overPosition;
    private TaskStatus startStatus;
    private TaskStatus overStatus;
}
