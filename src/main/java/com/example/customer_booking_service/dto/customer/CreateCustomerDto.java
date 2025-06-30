package com.example.customer_booking_service.dto.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCustomerDto {
    private String fullName;

    private String email;

    private String status;

    private int age;
}
