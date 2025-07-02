package com.example.customer_booking_service.service;

import com.example.customer_booking_service.service.impl.DateTimeServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeServiceImplTest {

    @Test
    void now_returns_fixed_time_from_clock() {
        Instant fixedInstant = Instant.parse("2025-07-02T10:15:30.00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));

        DateTimeServiceImpl dateTimeService = new DateTimeServiceImpl(fixedClock);

        LocalDateTime now = dateTimeService.now();
        
        LocalDateTime expected = LocalDateTime.of(2025, 7, 2, 10, 15, 30);
        assertThat(now).isEqualTo(expected);
    }
}
