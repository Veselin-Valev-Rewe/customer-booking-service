package com.example.customerbookingservice.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
    private long id;

    private String name;

    private String address;

    private LocalDateTime created;

    private LocalDateTime updated;

    private String shortCode;
}
