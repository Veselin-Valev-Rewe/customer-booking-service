package com.example.customer_booking_service.dto.brand;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateBrandDto {
    private String name;

    private String address;

    private LocalDate created;

    private LocalDate updated;

    private String shortCode;
}