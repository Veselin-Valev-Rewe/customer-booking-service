package com.example.customer_booking_service.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBrandDto {
    @NotBlank(message = "Name is required")
    @Size(max = 32, message = "Name can't exceed 32 characters")
    private String name;

    @NotBlank(message = "Address is required")
    @Size(max = 100, message = "Name can't exceed 100 characters")
    private String address;

    @NotBlank(message = "Short Code is required")
    @Size(max = 3, message = "Name can't exceed 3 characters")
    private String shortCode;
}