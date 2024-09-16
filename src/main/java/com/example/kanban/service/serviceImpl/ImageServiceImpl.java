package com.example.kanban.service.serviceImpl;

import com.example.kanban.entity.Image;
import com.example.kanban.entity.Task;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.repository.ImageRepository;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final TaskRepository taskRepository;
    private final ImageRepository imageRepository;

    @Override
    public void saveImagesForTask(String taskId, List<String> imageUrls) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BusinessException("Task not found", ErrorCode.TASK_NOT_FOUND));

        List<Image> images = imageUrls.stream()
                .map(url -> new Image(null, url, task))
                .collect(Collectors.toList());

        imageRepository.saveAll(images);  // Perform bulk save
    }

    @Override
    public List<String> getImageUrlsForTask(String taskId) {
        return imageRepository.findAllImageUrlsByTaskId(taskId);
    }
}
