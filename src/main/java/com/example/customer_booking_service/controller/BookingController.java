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
        var createdBooking = BookingDto.builder().build();
        URI location = URI.create("/bookings/" + createdBooking.getId());
        return ResponseEntity.created(location).body(createdBooking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable long id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/brands/{brandId}")
    public ResponseEntity<Void> addBrandToBooking(@PathVariable long id, @PathVariable long brandId) {
        return ResponseEntity.noContent().build();
    }
}
