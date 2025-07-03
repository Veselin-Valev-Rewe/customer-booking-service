package com.example.customer_booking_service.integration.controller;

import com.example.customer_booking_service.data.entity.Booking;
import com.example.customer_booking_service.data.entity.Brand;
import com.example.customer_booking_service.data.entity.Customer;
import com.example.customer_booking_service.data.enums.BookingStatus;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.data.repository.CustomerRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.booking.CreateBookingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookingControllerIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateBooking() {
        // Given
        var customer = Customer.builder()
                .fullName("Booking Test")
                .email("bookingtest@example.com")
                .build();
        customer = customerRepository.save(customer);

        var createDto = CreateBookingDto.builder()
                .title("Test Booking")
                .description("Test description")
                .status("PENDING")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .customerId(customer.getId())
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/bookings",
                HttpMethod.POST,
                new HttpEntity<>(createDto),
                new ParameterizedTypeReference<BookingDto>() {
                });

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Booking");
        assertThat(response.getBody().getCustomerId()).isEqualTo(customer.getId());

        Optional<Booking> bookingOpt = bookingRepository.findById(response.getBody().getId());
        assertThat(bookingOpt).isPresent();
        assertThat(bookingOpt.get().getTitle()).isEqualTo("Test Booking");
    }

    @Test
    void shouldDeleteBooking() {
        // Given
        var customer = Customer.builder()
                .fullName("Delete Test")
                .email("deletetest@example.com")
                .build();
        customer = customerRepository.save(customer);

        var booking = Booking.builder()
                .title("Delete Booking")
                .description("Delete description")
                .status(BookingStatus.CANCELLED)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .customer(customer)
                .build();
        booking = bookingRepository.save(booking);

        // When
        var response = restTemplate.exchange(
                "/api/bookings/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                booking.getId()
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(bookingRepository.existsById(booking.getId())).isFalse();
    }

    @Test
    void shouldReturnNotFoundOnDelete_ForNonExistingBooking() {
        // When
        var response = restTemplate.exchange(
                "/api/bookings/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                9999L
        );

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldAddBrandToBooking() {
        // Given
        var customer = Customer.builder()
                .fullName("Brand Customer")
                .email("brandcustomer@example.com")
                .build();
        customer = customerRepository.save(customer);

        var booking = Booking.builder()
                .title("Brand Booking")
                .description("Brand description")
                .status(BookingStatus.CANCELLED)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .customer(customer)
                .build();
        booking = bookingRepository.save(booking);

        var brand = brandRepository.save(
                Brand.builder()
                        .name("Test Brand")
                        .build()
        );

        // When
        var response = restTemplate.exchange(
                "/api/bookings/{id}/brands/{brandId}",
                HttpMethod.PATCH,
                null,
                Void.class,
                booking.getId(),
                brand.getId()
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        var updatedBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(updatedBooking.getBrand().getId()).isEqualTo(brand.getId());
    }

    @Test
    void shouldReturnNotFound_WhenAddingBrandToNonExistingBooking() {
        // Given
        var brand = brandRepository.save(
                Brand.builder()
                        .name("Test Brand")
                        .build()
        );

        // When
        var response = restTemplate.exchange(
                "/api/bookings/{bookingId}/brands/{brandId}",
                HttpMethod.PATCH,
                null,
                Void.class,
                9999L,  // non-existing booking ID
                brand.getId()
        );

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldReturnNotFound_WhenAddingNonExistingBrandToBooking() {
        // Given
        var customer = Customer.builder()
                .fullName("Brand Customer")
                .email("brandcustomer@example.com")
                .build();
        customer = customerRepository.save(customer);

        var booking = Booking.builder()
                .title("Brand Booking")
                .description("Brand description")
                .status(BookingStatus.CANCELLED)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .customer(customer)
                .build();
        booking = bookingRepository.save(booking);

        // When
        var response = restTemplate.exchange(
                "/api/bookings/{id}/brands/{brandId}",
                HttpMethod.PATCH,
                null,
                Void.class,
                booking.getId(),
                9999L
        );

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}
