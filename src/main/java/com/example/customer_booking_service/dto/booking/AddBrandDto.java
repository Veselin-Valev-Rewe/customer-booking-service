package com.example.customer_booking_service.dto.booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddBrandDto {
    private long brandId;

    private long bookingId;
}
