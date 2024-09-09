package com.example.kanban.entity;

import com.example.kanban.entity.enums.TaskPriority;
import com.example.kanban.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String description;
    private Instant dateTimeStart;
    private Instant dateTimeFinish;
    private Boolean isPublic;

    @CreatedBy
    private String creatorDisplayName;

    @CreatedBy
    @Column(updatable = false)
    private String createdByUsername;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;


    @ManyToOne
    @JoinColumn(name = "assignedUserId", nullable = true)
    private User assignedUser;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
}
