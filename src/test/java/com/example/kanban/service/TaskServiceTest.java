package com.example.kanban.service;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.MoveTaskRequest;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.request.TaskUpdateRequest;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.entity.Task;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.TaskStatus;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.mapper.TaskMapper;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.repository.UserRepository;
import com.example.kanban.service.serviceImpl.TaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.AuditorAware;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TaskServiceTest {
    @InjectMocks
    TaskServiceImpl taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuditorAware<AuthUser> auditorAware;

    @Mock
    TaskMapper taskMapper;

    private AuthUser currentUser;
    private Task task1, task2, task3, task4, task;
    private TaskCreateRequest taskCreateRequest;
    private TaskResponse taskResponse;

    @BeforeEach
    void setUp() {
        currentUser = new AuthUser();
        currentUser.setDisplayName("Test User");
        currentUser.setUsername("testuser");

        task1 = Task.builder()
                .id("1")
                .position(0)
                .status(TaskStatus.TO_DO)
                .assignedUser(User.builder().username("tester").build())
                .build();

        task2 = Task.builder()
                .id("2")
                .position(1)
                .status(TaskStatus.TO_DO)
                .assignedUser(User.builder().username("tester").build())
                .build();

        task3 = Task.builder()
                .id("3")
                .position(2)
                .status(TaskStatus.TO_DO)
                .assignedUser(User.builder().username("tester").build())
                .build();

        task4 = Task.builder()
                .id("4")
                .position(3)
                .status(TaskStatus.TO_DO)
                .assignedUser(User.builder().username("tester").build())
                .build();
    }

    @Test
    void moveTaskDownSameColumn_success() {
        // Simulate task repository
        Mockito.when(taskRepository.findById("1")).thenReturn(Optional.of(task1));
        Mockito.when(taskRepository.findByStatusAndPositionBetween(TaskStatus.TO_DO, 1, 2))
                .thenReturn(Arrays.asList(task2, task3));

        taskService.moveTaskDown(task1, 0, 2, TaskStatus.TO_DO);

        // Assert
        Assertions.assertEquals(0, task2.getPosition());
        Assertions.assertEquals(1, task3.getPosition());
        Assertions.assertEquals(2, task1.getPosition());
        Assertions.assertEquals(3, task4.getPosition());
    }

    @Test
    void moveTaskUpSameColumn_success() {
// Simulate task repository
        Mockito.when(taskRepository.findById("3")).thenReturn(Optional.of(task3));
        Mockito.when(taskRepository.findByStatusAndPositionBetween(TaskStatus.TO_DO, 2, 0))
                .thenReturn(Arrays.asList(task2, task3));

        task3.setPosition(0);
        task1.setPosition(task1.getPosition() + 1);
        task2.setPosition(task2.getPosition() + 1);

        taskService.moveTaskUp(task3, 2, 0, TaskStatus.TO_DO);

        // Assert
        Assertions.assertEquals(0, task3.getPosition());
        Assertions.assertEquals(1, task1.getPosition());
        Assertions.assertEquals(2, task2.getPosition());
        Assertions.assertEquals(3, task4.getPosition());
    }

    @Test
    void moveTaskDifferentColumn_success() {

    }

    @Test
    public void createTask_validRequest_success() {
        // Arrange
        TaskCreateRequest request = new TaskCreateRequest();
        request.setName("Test Task");
        request.setDescription("Test Description");
        request.setDateTimeFinish(Instant.now().plus(1, ChronoUnit.DAYS));
        request.setIsPublic(false);
        request.setStatus(TaskStatus.TO_DO);

        AuthUser currentUser = new AuthUser();
        currentUser.setUsername("tester");
        currentUser.setDisplayName("Test User");

        // Create a mock Task object
        Task newTask = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dateTimeStart(Instant.now())
                .dateTimeFinish(request.getDateTimeFinish())
                .isPublic(request.getIsPublic())
                .status(request.getStatus())
                .creatorDisplayName(currentUser.getDisplayName())
                .createdByUsername(currentUser.getUsername())
                .position(1) // This will be set in the service later
                .build();

        User assignedUser = User.builder().username("tester").build();

        Mockito.when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(currentUser));
        Mockito.when(taskRepository.findMaxPositionByStatus(request.getStatus())).thenReturn(0); // No tasks present
        Mockito.when(userRepository.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(assignedUser));
        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // Mock taskMapper to return newTask when taskCreateRequestToTask is called
        Mockito.when(taskMapper.taskCreateRequestToTask(request)).thenReturn(newTask);

        // Mock the taskMapper's conversion from Task to TaskResponse
        TaskResponse taskResponse = new TaskResponse("1", "Test Task", "Test Description",
                newTask.getDateTimeStart(), newTask.getDateTimeFinish(), newTask.getIsPublic(),
                newTask.getCreatorDisplayName(), newTask.getCreatedByUsername(), newTask.getStatus(),
                newTask.getPosition(), newTask.getPriority(), null, null, null);

        Mockito.when(taskMapper.taskToTaskResponse(newTask)).thenReturn(taskResponse);

        // Act
        TaskResponse response = taskService.createTask(request);
        log.info("Create task response " + response);

        // Assert
        Assertions.assertNotNull(response);
        assertEquals("Test Task", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals(currentUser.getDisplayName(), response.getCreatorDisplayName());
        assertEquals(currentUser.getDisplayName(), response.getCreatorDisplayName());
        assertEquals(request.getIsPublic(), response.getIsPublic());
    }

    @Test
    public void createTask_currentUserNull_fail() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setName("Test Task");
        request.setDescription("Test Description");
        request.setDateTimeFinish(Instant.now().plus(1, ChronoUnit.DAYS));
        request.setIsPublic(false);
        request.setStatus(TaskStatus.TO_DO);

        Mockito.when(auditorAware.getCurrentAuditor()).thenReturn(Optional.empty()); // Return null

            BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
                taskService.createTask(request);
            });

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        Assertions.assertEquals("Unable to get current user", exception.getMessage());
    }

    @Test
    public void updateTask_userWithoutPermission_fail() {
        String taskId = "1";

        TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest();
        taskUpdateRequest.setName("Updated title");
        taskUpdateRequest.setDescription("Update description here");

        AuthUser currentUser = new AuthUser();
        currentUser.setUsername("unauthorized");
        currentUser.setDisplayName("Unauthorized User");

        Task task = Task.builder()
                .name("Title")
                .description("Description")
                .createdByUsername("creatorUser")
                .assignedUser(User.builder().username("assignee").build())
                .build();

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            taskService.updateTask(taskId, taskUpdateRequest, currentUser);
        });

        Assertions.assertEquals("This user does not have permission to update this task.", exception.getMessage());
        Assertions.assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
    }

    @Test
    public void takeTask_nobodyAssigned_success() {
        String taskId = "1";

        AuthUser currentUser = new AuthUser();
        currentUser.setUsername("new assignee");
        currentUser.setDisplayName("Assignee B");

        Task task = Task.builder()
                .name("Title")
                .description("Description")
                .createdByUsername("creatorUser")
                .assignedUser(null)
                .build();

        User assignedUser = User.builder()
                .username(currentUser.getUsername())
                .displayName(currentUser.getDisplayName())
                .build();

        Mockito.when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(currentUser));
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(assignedUser));

        taskService.takeTask(taskId, currentUser);

        Assertions.assertEquals(task.getAssignedUser().getUsername(), currentUser.getUsername());
        Assertions.assertEquals(task.getAssignedUser().getDisplayName(), currentUser.getDisplayName());

        // Verify that taskRepository.save() was called to persist the change
        Mockito.verify(taskRepository).save(task);

    }

    @Test
    public void takeTask_someoneAssigned_fail() {
        String taskId = "1";

        AuthUser currentUser = new AuthUser();
        currentUser.setUsername("new assignee");
        currentUser.setDisplayName("Assignee B");

        Task task = Task.builder()
                .name("Title")
                .description("Description")
                .createdByUsername("creatorUser")
                .assignedUser(User.builder().username("assignee A").build())
                .build();

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            taskService.takeTask(taskId, currentUser);
        });

        Assertions.assertEquals(ErrorCode.TASK_ALREADY_TAKEN, exception.getErrorCode());
        Assertions.assertEquals("The task has already been taken by someone!", exception.getMessage());
    }
}
