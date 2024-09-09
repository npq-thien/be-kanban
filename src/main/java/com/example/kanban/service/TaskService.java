package com.example.kanban.service;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskCreateRequest request);
    ApiResponse getAllTasks();
    ApiResponse getUserTasks(String id);
    TaskResponse updateTask(String taskId, TaskUpdateRequest request, AuthUser currentUser);
    TaskResponse takeTask(String taskId, AuthUser currentUser);
    TaskResponse dropTask(String taskId, AuthUser currentUser);
}
