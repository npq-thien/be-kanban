package com.example.kanban.exception;

public enum ErrorCode {
    // Authentication Errors
    UNAUTHENTICATED,
    UNAUTHORIZED,
    TOKEN_EXPIRED,
    ACCESS_DENIED,
    USER_NOT_AUTHORIZED,

    // Validation Errors
    VALIDATION_FAILED,
    MISSING_REQUIRED_FIELD,
    INVALID_FIELD_FORMAT,
    PASSWORD_TOO_WEAK,

    // Resource Not Found
    USER_NOT_FOUND,
    TASK_NOT_FOUND,

    // Conflict Errors
    USER_ALREADY_EXISTS,
    RESOURCE_ALREADY_EXISTS,
    TASK_ALREADY_TAKEN,

    // Database and System Errors
    DATABASE_ERROR,
    SERVICE_UNAVAILABLE,
    INTERNAL_SERVER_ERROR,

    // Business Logic Errors
    CANNOT_DELETE_ACTIVE_BOARD,
    TASK_LIMIT_EXCEEDED
}
