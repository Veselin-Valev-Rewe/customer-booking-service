package com.example.customerbookingservice.service;

import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.booking.CreateBookingDto;
import jakarta.validation.Valid;

import java.util.Optional;

public interface BookingService {
    Optional<BookingDto> createBooking(@Valid CreateBookingDto bookingDto);

    boolean deleteCustomer(long id);

    boolean addBrandToBooking(long id, long brandId);
}
