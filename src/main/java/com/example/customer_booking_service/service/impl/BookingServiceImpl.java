package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.booking.CreateBookingDto;
import com.example.customer_booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto createBooking(CreateBookingDto bookingDto) {
        return null;
    }

    @Override
    public boolean deleteCustomer(long id) {
        return false;
    }

    @Override
    public boolean addBrandToBooking(long id, long brandId) {
        return false;
    }
}
