package com.example.kanban.repository;

import com.example.kanban.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    @Query("SELECT i.imageUrl FROM Image i WHERE i.task.id = :taskId")
    List<String> findAllImageUrlsByTaskId(@Param("taskId") String taskId);
}
