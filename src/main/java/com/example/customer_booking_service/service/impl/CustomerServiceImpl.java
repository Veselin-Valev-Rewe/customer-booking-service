package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.repository.CustomerRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.customer.CreateCustomerDto;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import com.example.customer_booking_service.dto.customer.UpdateCustomerDto;
import com.example.customer_booking_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<CustomerDto> getCustomers() {
        return List.of();
    }

    @Override
    public Optional<CustomerDto> getCustomerById(long id) {
        return Optional.empty();
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto customerDto) {
        return null;
    }

    @Override
    public Optional<CustomerDto> updateCustomer(UpdateCustomerDto customerDto) {
        return Optional.empty();
    }

    @Override
    public boolean deleteCustomer(long id) {
        return false;
    }

    @Override
    public List<BookingDto> getCustomerBookings(long id) {
        return List.of();
    }
}
