package com.example.customer_booking_service.dto.booking;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBrandDto {
    @Min(value = 1, message = "Value must be a positive number.")
    private long brandId;

    @Min(value = 1, message = "Value must be a positive number.")
    private long bookingId;
}
