package com.example.customer_booking_service.service;

import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.booking.CreateBookingDto;
import jakarta.validation.Valid;

public interface BookingService {

    BookingDto createBooking(@Valid CreateBookingDto bookingDto);

    boolean deleteCustomer(long id);

    boolean addBrandToBooking(long id, long brandId);
}
