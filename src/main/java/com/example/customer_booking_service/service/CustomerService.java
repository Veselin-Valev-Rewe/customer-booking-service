package com.example.customer_booking_service.service;

import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.customer.CreateCustomerDto;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import com.example.customer_booking_service.dto.customer.UpdateCustomerDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDto> getCustomers();

    Optional<CustomerDto> getCustomerById(long id);

    CustomerDto createCustomer(@Valid CreateCustomerDto customerDto);

    Optional<CustomerDto> updateCustomer(@Valid UpdateCustomerDto customerDto);

    boolean deleteCustomer(long id);

    List<BookingDto> getCustomerBookings(long id);
}
