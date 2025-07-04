package com.example.customerbookingservice.unit.service;

import com.example.customerbookingservice.data.entity.Booking;
import com.example.customerbookingservice.data.entity.Brand;
import com.example.customerbookingservice.data.entity.Customer;
import com.example.customerbookingservice.data.enums.BookingStatus;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.BrandRepository;
import com.example.customerbookingservice.data.repository.CustomerRepository;
import com.example.customerbookingservice.dto.booking.CreateBookingDto;
import com.example.customerbookingservice.service.DateTimeService;
import com.example.customerbookingservice.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private CustomerRepository customerRepository;
    private BookingRepository bookingRepository;
    private BrandRepository brandRepository;
    private DateTimeService dateTimeService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        bookingRepository = mock(BookingRepository.class);
        brandRepository = mock(BrandRepository.class);
        dateTimeService = mock(DateTimeService.class);
        ModelMapper modelMapper = new ModelMapper();
        bookingService = new BookingServiceImpl(customerRepository, bookingRepository, brandRepository, modelMapper, dateTimeService);
    }

    @Test
    void createBooking_customerExists_returnsBookingDto() {
        // Given
        var dto = CreateBookingDto.builder()
                .title("Consultation")
                .description("Discuss service options")
                .status(BookingStatus.CANCELLED.name())
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 2))
                .customerId(1L)
                .build();

        var customer = Customer.builder()
                .id(1L)
                .fullName("Alice")
                .email("Example@gmail.com")
                .build();

        var now = LocalDateTime.now();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(dateTimeService.now()).thenReturn(now);
        when(bookingRepository.save(any())).thenAnswer(inv -> {
            Booking booking = inv.getArgument(0);
            booking.setId(100L);
            return booking;
        });

        // When
        var result = bookingService.createBooking(dto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Consultation");
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_customerNotFound_returnsEmpty() {
        // Given
        var dto = CreateBookingDto.builder()
                .customerId(404L)
                .title("Not created")
                .build();

        when(customerRepository.findById(404L)).thenReturn(Optional.empty());

        // When
        var result = bookingService.createBooking(dto);

        // Then
        assertThat(result).isEmpty();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void deleteCustomer_bookingExists_deletesAndReturnsTrue() {
        // Given
        when(bookingRepository.existsById(10L)).thenReturn(true);

        // When
        var result = bookingService.deleteCustomer(10L);

        // Then
        assertThat(result).isTrue();
        verify(bookingRepository).deleteById(10L);
    }

    @Test
    void deleteCustomer_bookingNotExists_returnsFalse() {
        // Given
        when(bookingRepository.existsById(11L)).thenReturn(false);

        // When
        var result = bookingService.deleteCustomer(11L);

        // Then
        assertThat(result).isFalse();
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    @Test
    void addBrandToBooking_bothExist_setsBrandAndReturnsTrue() {
        // Given
        var bookingId = 1L;
        var brandId = 2L;

        var booking = Booking.builder()
                .id(bookingId)
                .title("Test Booking")
                .build();

        var brand = Brand.builder()
                .id(brandId)
                .name("Test Brand")
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        when(dateTimeService.now()).thenReturn(LocalDateTime.now());

        // When
        var result = bookingService.addBrandToBooking(bookingId, brandId);

        // Then
        assertThat(result).isTrue();
        assertThat(booking.getBrand()).isEqualTo(brand);
        verify(bookingRepository).save(booking);
    }

    @Test
    void addBrandToBooking_bookingNotFound_returnsFalse() {
        // Given
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        var result = bookingService.addBrandToBooking(999L, 1L);

        // Then
        assertThat(result).isFalse();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void addBrandToBooking_brandNotFound_returnsFalse() {
        // Given
        var booking = Booking.builder().id(1L).build();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        var result = bookingService.addBrandToBooking(1L, 999L);

        // Then
        assertThat(result).isFalse();
        verify(bookingRepository, never()).save(any());
    }
}
