package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.entity.Customer;
import com.example.customer_booking_service.data.enums.CustomerStatus;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.CustomerRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.customer.CreateCustomerDto;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import com.example.customer_booking_service.dto.customer.UpdateCustomerDto;
import com.example.customer_booking_service.service.CustomerService;
import com.example.customer_booking_service.service.DateTimeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final DateTimeService dateTimeService;

    @Override
    public List<CustomerDto> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .toList();
    }

    @Override
    public Optional<CustomerDto> getCustomerById(long id) {
        return customerRepository.findById(id)
                .map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto customerDto) {
        var customer = modelMapper.map(customerDto, Customer.class);
        var dateNow = dateTimeService.now().toLocalDate();
        customer.setCreated(dateNow);
        customer.setUpdated(dateNow);

        return modelMapper.map(customerRepository.save(customer), CustomerDto.class);
    }

    @Override
    public Optional<CustomerDto> updateCustomer(UpdateCustomerDto customerDto) {
        return customerRepository.findById(customerDto.getId())
                .map(existingCustomer -> {
                    existingCustomer.setFullName(customerDto.getFullName());
                    existingCustomer.setStatus(CustomerStatus.valueOf(customerDto.getStatus()));
                    existingCustomer.setEmail(customerDto.getEmail());
                    existingCustomer.setAge(customerDto.getAge());
                    existingCustomer.setUpdated(dateTimeService.now().toLocalDate());

                    var savedCustomer = customerRepository.save(existingCustomer);
                    return modelMapper.map(savedCustomer, CustomerDto.class);
                });
    }

    @Override
    public boolean deleteCustomer(long id) {
        if (!customerRepository.existsById(id)) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BookingDto> getCustomerBookings(long id) {
        return bookingRepository.findByCustomerId(id)
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .toList();
    }
}
