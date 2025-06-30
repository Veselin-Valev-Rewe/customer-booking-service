package com.example.customer_booking_service.controller;

import com.example.customer_booking_service.dto.booking.AddBrandDto;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.booking.CreateBookingDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {
    @PostMapping("/create")
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid CreateBookingDto bookingDto) {
        var createdBooking = BookingDto.builder().build();
        URI location = URI.create("/booking/" + createdBooking.getId());
        return ResponseEntity.created(location).body(createdBooking);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBooking(@RequestParam long id) {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/addBrand")
    public ResponseEntity<Void> addBrandToBooking(@RequestBody @Valid AddBrandDto addBrandDto) {
        return ResponseEntity.noContent().build();
    }
}
