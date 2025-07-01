package com.example.customer_booking_service.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
    private long id;

    private String name;

    private String address;

    private LocalDate created;

    private LocalDate updated;

    private String shortCode;
}
