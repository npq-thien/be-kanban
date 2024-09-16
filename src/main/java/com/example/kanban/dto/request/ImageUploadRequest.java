package com.example.kanban.dto.request;

import lombok.Data;

@Data
public class ImageUploadRequest {
    private String taskId;
    private String imageUrl;
}