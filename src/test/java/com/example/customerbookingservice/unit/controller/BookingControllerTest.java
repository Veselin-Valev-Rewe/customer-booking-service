package com.example.customerbookingservice.unit.controller;

import com.example.customerbookingservice.controller.BookingController;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.booking.CreateBookingDto;
import com.example.customerbookingservice.service.BookingService;
import com.example.customerbookingservice.service.DateTimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DateTimeService dateTimeService;

    @Test
    void createBooking_successful() throws Exception {
        // Given
        var createDto = CreateBookingDto.builder()
                .title("Test Booking")
                .description("Description")
                .status("CANCELLED")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 2))
                .customerId(1L)
                .build();

        var savedDto = BookingDto.builder()
                .id(1L)
                .title("Test Booking")
                .description("Description")
                .status(com.example.customerbookingservice.data.enums.BookingStatus.CANCELLED)
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 2))
                .customerId(1L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        given(bookingService.createBooking(any())).willReturn(Optional.of(savedDto));

        // When and Then
        mockMvc.perform(post("/api/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/bookings/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Booking")));
    }

    @Test
    void createBooking_notFound() throws Exception {
        // Given
        var createDto = CreateBookingDto.builder()
                .title("Invalid")
                .status("PENDING")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .customerId(999L)
                .build();

        given(bookingService.createBooking(any())).willReturn(Optional.empty());

        // When and Then
        mockMvc.perform(post("/api/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBooking_successful() throws Exception {
        // Given
        given(bookingService.deleteCustomer(1L)).willReturn(true);

        // When and Then
        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBooking_notFound() throws Exception {
        // Given
        given(bookingService.deleteCustomer(999L)).willReturn(false);

        // When and Then
        mockMvc.perform(delete("/api/bookings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBrandToBooking_successful() throws Exception {
        given(bookingService.addBrandToBooking(1L, 10L)).willReturn(true);

        mockMvc.perform(patch("/api/bookings/1/brands/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void addBrandToBooking_notFound() throws Exception {
        // Given
        given(bookingService.addBrandToBooking(1L, 999L)).willReturn(false);

        // When and Then
        mockMvc.perform(patch("/api/bookings/1/brands/999"))
                .andExpect(status().isNotFound());
    }
}
