package com.example.customer_booking_service.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String error,
        int status,
        String path,
        LocalDateTime timestamp
) {
}
