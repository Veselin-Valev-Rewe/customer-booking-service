package com.example.customer_booking_service.dto.booking;

import com.example.customer_booking_service.data.enums.BookingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateBookingDto {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Status is required")
    private BookingStatus status;

    @NotBlank(message = "Start Date is required")
    private LocalDate startDate;

    @NotBlank(message = "End Date is required")
    private LocalDate endDate;

    @Min(value = 1, message = "Value must be a positive number.")
    private long customerId;
}
