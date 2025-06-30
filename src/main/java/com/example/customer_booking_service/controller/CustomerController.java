package com.example.customer_booking_service.controller;

import com.example.customer_booking_service.dto.customer.CreateCustomerDto;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import com.example.customer_booking_service.dto.customer.UpdateCustomerDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    @GetMapping("/all")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/get")
    public ResponseEntity<CustomerDto> getCustomerById(@RequestParam long id) {
        return ResponseEntity.ok(CustomerDto.builder().build());
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CreateCustomerDto customerDto) {
        var createdCustomer = CustomerDto.builder().build();
        URI location = URI.create("/customers/" + createdCustomer.getId());
        return ResponseEntity.created(location).body(createdCustomer);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody @Valid UpdateCustomerDto customerDto) {
        return ResponseEntity.ok(CustomerDto.builder().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCustomer(@RequestParam long id) {
        return ResponseEntity.noContent().build();
    }
}
