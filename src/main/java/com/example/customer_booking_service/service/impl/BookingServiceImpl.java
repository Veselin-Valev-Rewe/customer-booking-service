package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.entity.Booking;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.data.repository.CustomerRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.booking.CreateBookingDto;
import com.example.customer_booking_service.service.BookingService;
import com.example.customer_booking_service.service.DateTimeService;
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
    private final DateTimeService dateTimeService;

    @Override
    public Optional<BookingDto> createBooking(CreateBookingDto bookingDto) {
        return customerRepository.findById(bookingDto.getCustomerId()).map(customer -> {
            var dateNow = dateTimeService.now().toLocalDate();
            var booking = modelMapper.map(bookingDto, Booking.class);
            booking.setCustomer(customer);
            booking.setRecordCreated(dateNow);
            booking.setRecordUpdated(dateNow);

            customer.getBookings().add(booking);
            customer.setUpdated(dateNow);

            customerRepository.save(customer);
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
        booking.setRecordCreated(dateTimeService.now().toLocalDate());
        bookingRepository.save(booking);
        return true;
    }
}
