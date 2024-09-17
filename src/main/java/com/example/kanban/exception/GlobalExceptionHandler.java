package com.example.kanban.exception;

import com.example.kanban.dto.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException exception) {
        HttpStatus status = switch (exception.getErrorCode()) {
            // Map ErrorCode to HTTP Status
            case USER_ALREADY_EXISTS, RESOURCE_ALREADY_EXISTS, TASK_ALREADY_TAKEN -> HttpStatus.CONFLICT;
            case USER_NOT_FOUND, TASK_NOT_FOUND, IMAGE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHENTICATED, UNAUTHORIZED, TOKEN_EXPIRED, ACCESS_DENIED, USER_NOT_AUTHORIZED,
                 USER_NOT_ASSIGNED_TASK -> HttpStatus.UNAUTHORIZED;
            case VALIDATION_FAILED, MISSING_REQUIRED_FIELD, INVALID_FIELD_FORMAT, PASSWORD_TOO_WEAK ->
                    HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };


        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .code(status.value())
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(status).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse apiResponse = ApiResponse.builder()
                .code(400)
                .message("Validation error")
                .data(errors)
                .timestamp(new Date())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Invalid argument provided")
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("Access denied ahihi")
                .timestamp(new Date())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(apiResponse);
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Data integrity violation")
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(apiResponse);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Entity not found")
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleGenericException(Exception exception) {
        ApiResponse apiResponse = ApiResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponse);
    }
}
