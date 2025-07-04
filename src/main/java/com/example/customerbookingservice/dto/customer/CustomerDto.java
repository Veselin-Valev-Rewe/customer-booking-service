package com.example.customerbookingservice.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime created;

    private LocalDateTime updated;
}
