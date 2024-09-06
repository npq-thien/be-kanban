package com.example.kanban.service.serviceImpl;

import com.example.kanban.dto.request.TaskCreateRequest;
import com.example.kanban.dto.response.TaskReponse;
import com.example.kanban.entity.Task;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public TaskReponse createTask(TaskCreateRequest request) {
        return new TaskReponse();
    }
}
