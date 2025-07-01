package com.example.customer_booking_service.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private long id;

    private String fullName;

    private String email;

    private String status;

    private int age;

    private LocalDate created;

    private LocalDate updated;
}
