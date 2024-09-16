package com.example.kanban.controller;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.MoveTaskRequest;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.service.ImageService;
import com.example.kanban.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;
    private final ImageService imageService;
    private final AuditorAware<AuthUser> auditorAware;


    @GetMapping
    ResponseEntity<ApiResponse> getAllTasks() {
        ApiResponse apiResponse = taskService.getAllTasks();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{userId}")
    ResponseEntity<ApiResponse> getUserTasks(@PathVariable String userId) {
        ApiResponse apiResponse = taskService.getUserTasks(userId);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{taskId}/images")
    ResponseEntity<ApiResponse> getTaskImages(@PathVariable String taskId) {
        List<String> imageUrls = imageService.getImageUrlsForTask(taskId);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .message("Fetch imageUrls for task successfully")
                .data(imageUrls)
                .build();

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

    @PostMapping("/{taskId}/images")
    ResponseEntity<ApiResponse> uploadImages(@PathVariable String taskId, @RequestBody List<String> imageUrls) {
        AuthUser currentUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        System.out.println("Received image URLs: " + imageUrls);

        imageService.saveImagesForTask(taskId, imageUrls);

        ApiResponse response = ApiResponse.builder()
                .code(200)
                .message("Images uploaded successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}")
    ResponseEntity<ApiResponse> updateTask(@PathVariable String taskId, @RequestBody @Valid TaskUpdateRequest request) {
        AuthUser currentUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        try {
            TaskResponse taskResponse = taskService.updateTask(taskId, request, currentUser);

            ApiResponse apiResponse = ApiResponse.builder()
                    .code(200)
                    .message("Update task successfully")
                    .data(taskResponse)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (BusinessException e) {
            ApiResponse errorResponse = ApiResponse.builder()
                    .code(1000)
                    .message("Update task failed")
                    .data(e.getMessage())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    @PutMapping("/take/{taskId}")
    ResponseEntity<ApiResponse> takeTask(@PathVariable String taskId) {
        AuthUser currentUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        try {
            TaskResponse taskResponse = taskService.takeTask(taskId, currentUser);

            ApiResponse apiResponse = ApiResponse.builder()
                    .code(200)
                    .message("Take task successfully")
                    .data(taskResponse)
                    .build();

            return ResponseEntity.ok(apiResponse);

        } catch (BusinessException e) {
            ApiResponse errorResponse = ApiResponse.builder()
                    .code(1000)
                    .message("Update task failed")
                    .data(e.getMessage())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    @PutMapping("/drop/{taskId}")
    ResponseEntity<ApiResponse> dropTask(@PathVariable String taskId) {
        AuthUser currentUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        try {
            TaskResponse taskResponse = taskService.dropTask(taskId, currentUser);

            ApiResponse apiResponse = ApiResponse.builder()
                    .code(200)
                    .message("Drop task successfully")
                    .data(taskResponse)
                    .build();

            return ResponseEntity.ok(apiResponse);

        } catch (BusinessException e) {
            ApiResponse errorResponse = ApiResponse.builder()
                    .code(1000)
                    .message("Drop task failed")
                    .data(e.getMessage())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    @PutMapping("/move")
    ResponseEntity<ApiResponse> dropTask(@RequestBody @Valid MoveTaskRequest request) {
        AuthUser currentUser = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        boolean taskResponse = taskService.moveTask(request, currentUser);

        if (taskResponse) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .code(200)
                    .message("Move task successfully")
                    .build();

            return ResponseEntity.ok(apiResponse);

        } else {
            ApiResponse errorResponse = ApiResponse.builder()
                    .code(1000)
                    .message("Move task failed")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

}
