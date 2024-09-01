package com.example.kanban.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @OneToMany(mappedBy = "board")
    private List<TaskList> taskLists;

    @ManyToMany
    @JoinTable(name = "Board_User",
            joinColumns = @JoinColumn(name = "boardId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users;
}
