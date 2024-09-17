package com.example.kanban.service;

import com.example.kanban.dto.response.ApiResponse;

import java.util.List;

public interface ImageService {
    void saveImagesForTask(String taskId, List<String> imageUrls);
    ApiResponse getImageUrlsForTask(String taskId);
     void deleteImage(String imageId);
}
