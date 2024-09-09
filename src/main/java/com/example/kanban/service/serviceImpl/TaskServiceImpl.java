package com.example.kanban.service.serviceImpl;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.entity.Task;
import com.example.kanban.entity.User;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.mapper.TaskMapper;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.repository.UserRepository;
import com.example.kanban.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final AuditorAware<AuthUser> auditorAware;

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        // Get display name of current logged in user
        AuthUser authUser = auditorAware.getCurrentAuditor().orElseThrow(() ->
                new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        Task newTask = taskMapper.taskCreateRequestToTask(request);

        newTask.setDateTimeStart(Instant.now());
        newTask.setCreatorDisplayName(authUser.getDisplayName());
        newTask.setCreatedByUsername(authUser.getUsername());
        taskRepository.save(newTask);

        return taskMapper.taskToTaskResponse(newTask);
    }

    @Override
    public ApiResponse getAllTasks() {
        List<TaskResponse> listTasks = taskRepository.findAll()
                .stream().map(taskMapper::taskToTaskResponse).toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("count", listTasks.size());
        responseData.put("tasks", listTasks);

        return ApiResponse.builder()
                .code(200)
                .message("Tasks retrieved successfully")
                .data(responseData)
                .timestamp(new Date())
                .build();
    }

    @Override
    public ApiResponse getUserTasks(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found", ErrorCode.UNAUTHENTICATED));

        List<TaskResponse> listTasks = taskRepository.findAllByCreatedByUsernameOrPublic(user.getUsername())
                .stream().map(taskMapper::taskToTaskResponse).toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("count", listTasks.size());
        responseData.put("tasks", listTasks);

        return ApiResponse.builder()
                .code(200)
                .message("User's tasks retrieved successfully")
                .data(responseData)
                .timestamp(new Date())
                .build();
    }

    @Override
    public TaskResponse updateTask(String taskId, TaskUpdateRequest request, AuthUser currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("Task not found", ErrorCode.TASK_NOT_FOUND));

        if (!task.getCreatedByUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("This user does not have permission to update this task.", ErrorCode.UNAUTHORIZED);
        }

        taskMapper.updateTaskFromRequest(request, task);
        Task updatedTask = taskRepository.save(task);

        return taskMapper.taskToTaskResponse(updatedTask);
    }

}
