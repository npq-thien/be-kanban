package com.example.kanban.service;

import java.util.List;

public interface ImageService {
    void saveImagesForTask(String taskId, List<String> imageUrls);
    List<String> getImageUrlsForTask(String taskId);
}
