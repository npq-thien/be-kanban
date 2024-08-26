package com.example.kanban.entity;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private Instant dateTimeStart;
    private Instant dateTimeFinish;
    private TaskStatus status;
    private TaskPriority priority;
}
