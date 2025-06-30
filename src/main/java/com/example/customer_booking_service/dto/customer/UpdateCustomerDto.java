package com.example.customer_booking_service.dto.customer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCustomerDto {
    private long id;

    private String fullName;

    private String email;

    private String status;

    private int age;
}
