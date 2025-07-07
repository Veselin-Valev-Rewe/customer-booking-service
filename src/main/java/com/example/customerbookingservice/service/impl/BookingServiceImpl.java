package com.example.customerbookingservice.service.impl;

import com.example.customerbookingservice.data.entity.Booking;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.BrandRepository;
import com.example.customerbookingservice.data.repository.CustomerRepository;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.booking.CreateBookingDto;
import com.example.customerbookingservice.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookingDto createBooking(CreateBookingDto bookingDto) {
        var customer = customerRepository.findById(bookingDto.getCustomerId());

        if (customer.isEmpty()) {
            throw new EntityNotFoundException("Customer not found");
        }
        var booking = modelMapper.map(bookingDto, Booking.class);
        booking.setId(null);
        booking.setCustomer(customer.get());

        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    public void deleteCustomer(long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking not found");
        }

        bookingRepository.deleteById(id);
    }

    @Override
    public BookingDto addBrand(long id, long brandId) {
        var bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isEmpty()) {
            throw new EntityNotFoundException("Booking not found");
        }

        var brandOptional = brandRepository.findById(brandId);

        if (brandOptional.isEmpty()) {
            throw new EntityNotFoundException("Brand not found");
        }

        var booking = bookingOptional.get();
        var brand = brandOptional.get();

        booking.setBrand(brand);
        bookingRepository.save(booking);

        return modelMapper.map(booking, BookingDto.class);
    }
}
