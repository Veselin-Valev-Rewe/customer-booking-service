package com.example.customerbookingservice.service.impl;

import com.example.customerbookingservice.service.DateTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DateTimeServiceImpl implements DateTimeService {
    private final Clock clock;


    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }
}
