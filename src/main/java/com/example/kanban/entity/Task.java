package com.example.kanban.entity;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "taskListId", nullable = false)
    private TaskList taskList;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "Task_User",
    joinColumns = @JoinColumn(name = "taskId"),
    inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users;
}
