package com.example.kanban.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Kanban API",
                version = "0.1",
                description = "API documentation for the Kanban application"
        )
)
public class OpenApiConfig {
}