package com.example.kanban.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "boardId", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "taskList")
    private List<Task> lists;
}
