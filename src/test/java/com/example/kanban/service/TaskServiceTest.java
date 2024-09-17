package com.example.kanban.service;

import com.example.kanban.config.security.AuthUser;
import com.example.kanban.dto.request.MoveTaskRequest;
import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.entity.Task;
import com.example.kanban.entity.User;
import com.example.kanban.entity.enums.TaskStatus;
import com.example.kanban.mapper.TaskMapper;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.repository.UserRepository;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TaskServiceTest {
    @Mock
    TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditorAware<AuthUser> auditorAware;

    @Mock
    private TaskMapper taskMapper;

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

        task2.setPosition(task2.getPosition() - 1);
        task3.setPosition(task3.getPosition() - 1);
        task1.setPosition(2);

        taskService.moveTaskDown(task1, 0, 2, TaskStatus.TO_DO);

        // Assert
        Assertions.assertEquals(0, task2.getPosition());
        Assertions.assertEquals(1, task3.getPosition());
        Assertions.assertEquals(2, task1.getPosition());
        Assertions.assertEquals(3, task4.getPosition());
    }

    @Test
    void moveTaskUpSameColumn_success() {

    }

    @Test
    void moveTaskDifferentColumn_success() {

    }

    //@Test
    //void createTask_validRequest_success() {
    //    // GIVEN
    //    when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(currentUser));
    //    when(taskMapper.taskCreateRequestToTask(taskCreateRequest)).thenReturn(task);
    //    when(taskRepository.findMaxPositionByStatus(TaskStatus.TO_DO)).thenReturn(2);
    //    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
    //    when(taskRepository.save(any(Task.class))).thenReturn(task);
    //    when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);
    //
    //    // WHEN
    //    TaskResponse response = taskService.createTask(taskCreateRequest);
    //
    //    // THEN
    //    assertEquals("Test User", task.getCreatorDisplayName());
    //    assertEquals("testuser", task.getCreatedByUsername());
    //    assertEquals(3, task.getPosition()); // max position (2) + 1
    //}

    @Test
    void createTask_success() {
        // Given
        TaskCreateRequest request = new TaskCreateRequest();
        request.setName("Task Name");
        request.setDescription("Task Description");
        request.setDateTimeFinish(Instant.now().plus(1, ChronoUnit.DAYS));
        request.setIsPublic(false);
        request.setStatus(TaskStatus.TO_DO);

        AuthUser currentUser = new AuthUser();
        currentUser.setUsername("tester");
        currentUser.setDisplayName("Test User");

        Task newTask = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dateTimeFinish(request.getDateTimeFinish())
                .isPublic(request.getIsPublic())
                .status(request.getStatus())
                .creatorDisplayName(currentUser.getDisplayName())
                .createdByUsername(currentUser.getUsername())
                .position(1) // Assuming it's the highest position in the status
                .build();

        User assignedUser = User.builder().username("tester").build();

        // Mock the repository methods
        Mockito.when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(currentUser));
        Mockito.when(taskRepository.findMaxPositionByStatus(request.getStatus())).thenReturn(0); // No tasks present
        Mockito.when(userRepository.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(assignedUser));
        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        // When
        TaskResponse response = taskService.createTask(request);

        // Then
        //Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getName(), response.getName());
        Assertions.assertEquals(request.getDescription(), response.getDescription());
        Assertions.assertEquals(request.getDateTimeFinish(), response.getDateTimeFinish());
        Assertions.assertEquals(request.getIsPublic(), response.getIsPublic());
        Assertions.assertEquals(request.getStatus(), response.getStatus());
        Assertions.assertEquals(currentUser.getDisplayName(), response.getCreatorDisplayName());
        Assertions.assertEquals(currentUser.getUsername(), response.getCreatedByUsername());
        Assertions.assertEquals(1, response.getPosition());
        Assertions.assertEquals(assignedUser.getId(), response.getAssignedUserId());
        Assertions.assertEquals(assignedUser.getDisplayName(), response.getAssignedUserDisplayName());

        // Verify interactions
        Mockito.verify(taskRepository).save(any(Task.class));
        Mockito.verify(userRepository).findByUsername(currentUser.getUsername());
    }

}
