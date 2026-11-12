package com.example.cordo.entity.dto;

import java.time.LocalDateTime;

public record ExceptionDTO(
        int status,
        String message,
        String path,
        LocalDateTime timestamp
) {
    public ExceptionDTO(int status, String message, String path) {
        this(status, message, path, LocalDateTime.now());
    }
}
