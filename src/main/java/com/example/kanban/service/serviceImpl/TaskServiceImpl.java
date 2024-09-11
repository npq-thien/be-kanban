package com.example.kanban.service.serviceImpl;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.MoveTaskRequest;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.entity.Task;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.TaskStatus;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.mapper.TaskMapper;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.repository.UserRepository;
import com.example.kanban.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

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
        AuthUser currentUser = auditorAware.getCurrentAuditor().orElseThrow(() ->
                new BusinessException("Unable to get current user", ErrorCode.UNAUTHENTICATED));

        Task newTask = taskMapper.taskCreateRequestToTask(request);

        newTask.setDateTimeStart(Instant.now());
        newTask.setCreatorDisplayName(currentUser.getDisplayName());
        newTask.setCreatedByUsername(currentUser.getUsername());

        int maxPosition = taskRepository.findMaxPositionByStatus(request.getStatus());
        newTask.setPosition(maxPosition + 1); // Set to highest position

        // Set assigned user to the creator if the task is private
        if (!request.getIsPublic()) {
            User assignedUser = userRepository.findByUsername(currentUser.getUsername())
                    .orElseThrow(() -> new BusinessException("User not found", ErrorCode.UNAUTHENTICATED));

            newTask.setAssignedUser(assignedUser);
        }
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

        List<TaskResponse> listTasks = taskRepository.findAllByAssignedUsernameOrPublic(userId)
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

        // Only creator or assigned user can update task
        if (!task.getCreatedByUsername().equals(currentUser.getUsername())
                && (task.getAssignedUser() == null || !task.getAssignedUser().getUsername().equals(currentUser.getUsername()))) {
            throw new BusinessException("This user does not have permission to update this task.", ErrorCode.UNAUTHORIZED);
        }

        taskMapper.updateTaskFromRequest(request, task);
        Task updatedTask = taskRepository.save(task);

        return taskMapper.taskToTaskResponse(updatedTask);
    }

    @Transactional
    @Override
    public TaskResponse takeTask(String taskId, AuthUser currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("Task not found", ErrorCode.TASK_NOT_FOUND));

        if (task.getAssignedUser() != null) {
            throw new BusinessException("The task has already been taken by someone!", ErrorCode.TASK_ALREADY_TAKEN);
        }

        User assignedUser = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new BusinessException("User not found", ErrorCode.UNAUTHENTICATED));

        task.setAssignedUser(assignedUser);
        taskRepository.save(task);

        return taskMapper.taskToTaskResponse(task);
    }

    @Transactional
    @Override
    public TaskResponse dropTask(String taskId, AuthUser currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("Task not found", ErrorCode.TASK_NOT_FOUND));

        if (!task.getAssignedUser().getUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("Cannot drop because this user hasn't been assigned this task", ErrorCode.USER_NOT_ASSIGNED_TASK);
        }

        task.setAssignedUser(null);
        taskRepository.save(task);

        return taskMapper.taskToTaskResponse(task);
    }

    @Transactional
    @Override
    public boolean moveTask(MoveTaskRequest request, AuthUser currentUser) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new BusinessException("Task not found", ErrorCode.TASK_NOT_FOUND));

        // Only creator or assigned user can move task
        if (!task.getAssignedUser().getUsername().equals(currentUser.getUsername())) {
            throw new BusinessException("This user does not have permission to move task.", ErrorCode.UNAUTHORIZED);
        }

        int startPosition = request.getStartPosition();
        int overPosition = request.getOverPosition();
        TaskStatus startStatus = request.getStartStatus();
        TaskStatus overStatus = request.getOverStatus();

        // Move in the same column
        if (startStatus.equals(overStatus)) {
            // Task not actually moved, nothing to do
            if (overPosition == startPosition) {
                return false;
            }

            if (overPosition > startPosition) {
                moveTaskDown(task, startPosition, overPosition, overStatus);
            } else {
                moveTaskUp(task, startPosition, overPosition, overStatus);
            }
        } else { // Move task to different column
            int maxPositionInTargetColumn = taskRepository.findMaxPositionByStatus(overStatus);

            // Assign the task the next available position
            task.setPosition(maxPositionInTargetColumn + 1);
            task.setStatus(overStatus);
            taskRepository.save(task);

            // Adjust positions of tasks in the start column
            moveTasksInStartColumnAfterTaskRemoval(startPosition, startStatus);

            return true;
        }

        return true;
    }

    private void moveTaskUp(Task task, int startPosition, int overPosition, TaskStatus status) {
        List<Task> tasksInRange = taskRepository.findByStatusAndPositionBetween(
                status, overPosition, startPosition - 1
        );

        // Increment the position of all tasks in this range
        for (Task t : tasksInRange) {
            t.setPosition(t.getPosition() + 1);
            taskRepository.save(t);
        }

        task.setPosition(overPosition);
        taskRepository.save(task);
    }

    private void moveTaskDown(Task task, int startPosition, int overPosition, TaskStatus status) {
        List<Task> tasksBetween =
                taskRepository.findByStatusAndPositionBetween(status, startPosition + 1, overPosition);

        // Decrement the position of all tasks in this range
        for (Task t : tasksBetween) {
            t.setPosition(t.getPosition() - 1);
            taskRepository.save(t);
        }

        task.setPosition(overPosition);
        taskRepository.save(task);
    }

    private void moveTasksInStartColumnAfterTaskRemoval(int startPosition, TaskStatus startStatus) {
        // Fetch all tasks in the original column (startStatus) that are below the removed task's position
        List<Task> tasksToShift =
                taskRepository.findByStatusAndPositionGreaterThan(startStatus, startPosition);

        // Decrease the position of each of these tasks by 1 to fill the gap
        for (Task task : tasksToShift) {
            task.setPosition(task.getPosition() - 1);
        }

        // Save the updated tasks back to the repository
        taskRepository.saveAll(tasksToShift);
    }
}
