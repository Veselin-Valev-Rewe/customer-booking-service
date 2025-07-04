package com.example.customerbookingservice.service.impl;

import com.example.customerbookingservice.data.entity.Booking;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.BrandRepository;
import com.example.customerbookingservice.data.repository.CustomerRepository;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.booking.CreateBookingDto;
import com.example.customerbookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    public Optional<BookingDto> createBooking(CreateBookingDto bookingDto) {
        return customerRepository.findById(bookingDto.getCustomerId()).map(customer -> {
            var booking = modelMapper.map(bookingDto, Booking.class);
            booking.setId(null);
            booking.setCustomer(customer);

            return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
        });
    }

    @Override
    public boolean deleteCustomer(long id) {
        if (!bookingRepository.existsById(id)) {
            return false;
        }

        bookingRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean addBrandToBooking(long id, long brandId) {
        var bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isEmpty()) {
            return false;
        }

        var brandOptional = brandRepository.findById(brandId);

        if (brandOptional.isEmpty()) {
            return false;
        }

        var booking = bookingOptional.get();
        var brand = brandOptional.get();

        booking.setBrand(brand);
        bookingRepository.save(booking);
        return true;
    }
}
