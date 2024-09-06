package com.example.kanban.controller;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping
    ResponseEntity<ApiResponse> createTask(@RequestBody @Valid TaskCreateRequest request) {
        TaskResponse taskResponse = taskService.createTask(request);
        System.out.println("Task return" + taskResponse.toString());

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Create new task successfully")
                .data(taskResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
