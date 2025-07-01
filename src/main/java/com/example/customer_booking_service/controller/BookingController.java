package com.example.customer_booking_service.controller;

import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.booking.CreateBookingDto;
import com.example.customer_booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid CreateBookingDto bookingDto) {
        var bookingOptional = bookingService.createBooking(bookingDto);

        if (bookingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var booking = bookingOptional.get();
        URI location = URI.create("/api/bookings/" + booking.getId());

        return ResponseEntity.created(location).body(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) {
        return bookingService.deleteCustomer(id) ?
                ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/brands/{brandId}")
    public ResponseEntity<Void> addBrandToBooking(@PathVariable long id, @PathVariable long brandId) {
        return bookingService.addBrandToBooking(id, brandId) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
