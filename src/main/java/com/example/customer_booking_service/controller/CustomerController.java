package com.example.customer_booking_service.controller;

import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.customer.CreateCustomerDto;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import com.example.customer_booking_service.dto.customer.UpdateCustomerDto;
import com.example.customer_booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable long id) {
        return ResponseEntity.ok(CustomerDto.builder().build());
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CreateCustomerDto customerDto) {
        var createdCustomer = CustomerDto.builder().build();
        URI location = URI.create("/customers/" + createdCustomer.getId());
        return ResponseEntity.created(location).body(createdCustomer);
    }

    @PutMapping
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody @Valid UpdateCustomerDto customerDto) {
        return ResponseEntity.ok(CustomerDto.builder().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getCustomerBookings(@PathVariable long id) {
        return ResponseEntity.ok(List.of());
    }
}
