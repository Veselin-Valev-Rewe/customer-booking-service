package com.example.customerbookingservice.service;

import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.booking.CreateBookingDto;
import jakarta.validation.Valid;

public interface BookingService {
    BookingDto createBooking(@Valid CreateBookingDto bookingDto);

    void deleteCustomer(long id);

    BookingDto addBrand(long id, long brandId);
}
