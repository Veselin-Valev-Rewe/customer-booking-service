package com.example.customer_booking_service.dto.booking;

import com.example.customer_booking_service.data.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private long id;

    private String title;

    private String description;

    private BookingStatus status;

    private LocalDateTime created;

    private LocalDateTime updated;

    private LocalDate startDate;

    private LocalDate endDate;

    private String brandName;

    private long customerId;
}
