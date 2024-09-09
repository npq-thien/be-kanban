package com.example.kanban.repository;

import com.example.kanban.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT t FROM Task t WHERE t.createdByUsername = :username OR t.isPublic = true")
    List<Task> findAllByCreatedByUsernameOrPublic(@Param("username") String username);
    @Query("SELECT t FROM Task t WHERE t.assignedUser.id = :userId OR t.isPublic = true")
    List<Task> findAllByAssignedUsernameOrPublic(@Param("userId") String userId);
}
