package com.example.kanban.repository;

import com.example.kanban.entity.Task;
import com.example.kanban.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT t FROM Task t WHERE t.assignedUser.id = :userId OR t.isPublic = true")
    List<Task> findAllByAssignedUsernameOrPublic(@Param("userId") String userId);

    // Queries support move task function
    @Query("SELECT COALESCE(MAX(t.position), -1) FROM Task t WHERE t.status = :status")
    int findMaxPositionByStatus(@Param("status") TaskStatus status);

    List<Task> findByStatusAndPositionBetween(TaskStatus status, int startPosition, int endPosition);

    @Query("SELECT t FROM Task t WHERE t.status = :status AND t.position > :position")
    List<Task> findByStatusAndPositionGreaterThan(@Param("status") TaskStatus status, @Param("position") int position);

}
