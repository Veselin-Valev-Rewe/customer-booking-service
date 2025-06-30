package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
}
