package com.example.kanban.controller;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;
    private final AuditorAware<AuthUser> auditorAware;


    @GetMapping
    ResponseEntity<ApiResponse> getAllTasks() {
        ApiResponse apiResponse = taskService.getAllTasks();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    ResponseEntity<ApiResponse> createTask(@RequestBody @Valid TaskCreateRequest request) {
        TaskResponse taskResponse = taskService.createTask(request);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Create new task successfully")
                .data(taskResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{taskId}")
    ResponseEntity<ApiResponse> updateTask(@PathVariable String taskId, @RequestBody @Valid TaskUpdateRequest request) {
        AuthUser currentUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));
        System.out.println("REQUEST" + request);
        System.out.println("LOG HERE" + currentUser.getUsername() + ' ' + request.getCreatedByUsername());
        if (currentUser.getUsername().equals(request.getCreatedByUsername())) {
            TaskResponse taskResponse = taskService.updateTask(taskId, request);

            ApiResponse apiResponse = ApiResponse.builder()
                    .code(200)
                    .message("Update task successfully")
                    .data(taskResponse)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } else {
            throw new BusinessException("This user does not have permission to update this task", ErrorCode.UNAUTHORIZED);
        }
    }

}
