package com.example.customerbookingservice.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String error,
        int status,
        String path,
        LocalDateTime timestamp
) {
}
