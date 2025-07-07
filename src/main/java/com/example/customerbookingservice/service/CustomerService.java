package com.example.customerbookingservice.service;

import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.customer.CreateCustomerDto;
import com.example.customerbookingservice.dto.customer.CustomerDto;
import com.example.customerbookingservice.dto.customer.UpdateCustomerDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getCustomers(Pageable pageable);

    CustomerDto getCustomerById(long id);

    CustomerDto createCustomer(@Valid CreateCustomerDto customerDto);

    CustomerDto updateCustomer(@Valid UpdateCustomerDto customerDto);

    void deleteCustomer(long id);

    List<BookingDto> getCustomerBookings(long id, Pageable pageable);
}
