package com.example.kanban.service.serviceImpl;

import com.example.kanban.dto.response.ApiResponse;
import com.example.kanban.dto.response.ImageResponse;
import com.example.kanban.entity.Image;
import com.example.kanban.entity.Task;
import com.example.kanban.exception.BusinessException;
import com.example.kanban.exception.ErrorCode;
import com.example.kanban.mapper.ImageMapper;
import com.example.kanban.repository.ImageRepository;
import com.example.kanban.repository.TaskRepository;
import com.example.kanban.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final TaskRepository taskRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

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
    public ApiResponse getImageUrlsForTask(String taskId) {
        List<ImageResponse> images = imageRepository.findAllByTaskId(taskId)
                .stream().map(imageMapper::imageToImageResponse).toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("count", images.size());
        responseData.put("images", images);

        return ApiResponse.builder()
                .code(200)
                .message("Images retrieved successfully")
                .data(responseData)
                .timestamp(new Date())
                .build();
    }

    @Override
    public void deleteImage(String imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessException("Image not found", ErrorCode.IMAGE_NOT_FOUND));

        imageRepository.delete(image);
    }
}
