package com.example.kanban.service;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.response.TaskResponse;

public interface TaskService {
    public TaskResponse createTask(TaskCreateRequest request);
}
