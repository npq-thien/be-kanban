package com.example.kanban.service;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.response.TaskReponse;

public interface TaskService {
    public TaskReponse createTask(TaskCreateRequest request);
}
