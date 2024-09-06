package com.example.kanban.service.serviceImpl;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.response.TaskResponse;
import com.example.kanban.entity.Task;
import com.example.kanban.mapper.TaskMapper;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        Task newTask = taskMapper.taskCreateRequestToTask(request);

        newTask.setDateTimeStart(Instant.now());
        newTask.setCreatorDisplayName("Test task creator");
        taskRepository.save(newTask);
        System.out.println("Saved Task from DB: " + newTask);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(newTask.getId());
        taskResponse.setName(newTask.getName());
        taskResponse.setDescription(newTask.getDescription());
        taskResponse.setDateTimeStart(newTask.getDateTimeStart());
        taskResponse.setDateTimeFinish(newTask.getDateTimeFinish());
        taskResponse.setIsPublic(newTask.getIsPublic());
        taskResponse.setCreatorDisplayName(newTask.getCreatorDisplayName());
        taskResponse.setStatus(newTask.getStatus());
        taskResponse.setPriority(newTask.getPriority());

        return taskResponse;
    }

//    @Override
//    public TaskResponse createTask(TaskCreateRequest request) {
//        Task task = new Task();
//
//        // Map fields from the request to the task entity
//        task.setCreatorDisplayName("Test creator");
//        task.setName(request.getName());
//        task.setDescription(request.getDescription());
//        task.setDateTimeFinish(request.getDateTimeFinish());
//        task.setIsPublic(request.getIsPublic());
//        task.setStatus(request.getStatus());
//        task.setPriority(request.getPriority());
//
//        // Set other necessary fields like dateTimeStart
//        task.setDateTimeStart(Instant.now()); // Assuming you set the start time when creating the task
//
//        // Save the task
//        Task savedTask = taskRepository.save(task);
//
//        // Convert to TaskResponse
//        return taskMapper.taskToTaskResponse(savedTask);
//    }
}
