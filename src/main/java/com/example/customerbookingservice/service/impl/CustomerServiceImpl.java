package com.example.customerbookingservice.service.impl;

import com.example.customerbookingservice.data.entity.Customer;
import com.example.customerbookingservice.data.enums.CustomerStatus;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.CustomerRepository;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.customer.CreateCustomerDto;
import com.example.customerbookingservice.dto.customer.CustomerDto;
import com.example.customerbookingservice.dto.customer.UpdateCustomerDto;
import com.example.customerbookingservice.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CustomerDto> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .toList();
    }

    @Override
    public CustomerDto getCustomerById(long id) {
        var customer = getCustomer(id);
        return modelMapper.map(customer, CustomerDto.class);
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto customerDto) {
        var customer = modelMapper.map(customerDto, Customer.class);
        return modelMapper.map(customerRepository.save(customer), CustomerDto.class);
    }

    @Override
    public CustomerDto updateCustomer(UpdateCustomerDto customerDto) {
        var existingCustomer = getCustomer(customerDto.getId());
        existingCustomer.setFullName(customerDto.getFullName());
        existingCustomer.setStatus(CustomerStatus.valueOf(customerDto.getStatus()));
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setAge(customerDto.getAge());

        var savedCustomer = customerRepository.save(existingCustomer);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    @Override
    public void deleteCustomer(long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("Customer not found");
        }

        customerRepository.deleteById(id);
    }

    @Override
    public List<BookingDto> getCustomerBookings(long id, Pageable pageable) {
        return bookingRepository.findByCustomerId(id, pageable)
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .toList();
    }

    private Customer getCustomer(long id) {
        var customerOptional = customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new EntityNotFoundException("Customer not found");
        }
        return customerOptional.get();
    }
}
