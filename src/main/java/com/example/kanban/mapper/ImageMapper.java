package com.example.kanban.mapper;

import com.example.kanban.dto.response.ImageResponse;
import com.example.kanban.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(source = "task.id", target = "taskId")
    ImageResponse imageToImageResponse(Image image);
}
