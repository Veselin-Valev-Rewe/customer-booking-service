package com.example.customerbookingservice.unit.controller;

import com.example.customerbookingservice.controller.CustomerController;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.customer.CreateCustomerDto;
import com.example.customerbookingservice.dto.customer.CustomerDto;
import com.example.customerbookingservice.dto.customer.UpdateCustomerDto;
import com.example.customerbookingservice.service.CustomerService;
import com.example.customerbookingservice.service.DateTimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DateTimeService dateTimeService;

    @Test
    void getCustomers_returnsListOfCustomers() throws Exception {
        // Given
        var customers = List.of(
                CustomerDto
                        .builder()
                        .id(1L)
                        .fullName("John Doe")
                        .email("john@example.com")
                        .build(),

                CustomerDto
                        .builder()
                        .id(2L)
                        .fullName("Jane Smith")
                        .email("jane@example.com")
                        .build()
        );

        given(customerService.getCustomers()).willReturn(customers);

        // When and Then
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].fullName", is("John Doe")))
                .andExpect(jsonPath("$[1].email", is("jane@example.com")));
    }

    @Test
    void getCustomerById_found() throws Exception {
        // Given
        var dto = CustomerDto
                .builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .build();

        given(customerService.getCustomerById(1L)).willReturn(Optional.of(dto));

        // When and Then
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("John Doe")));
    }

    @Test
    void getCustomerById_notFound() throws Exception {
        // Given
        given(customerService.getCustomerById(99L)).willReturn(Optional.empty());

        // When and Then
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer_returnsCreatedCustomer() throws Exception {
        // Given
        var createDto = CreateCustomerDto
                .builder()
                .fullName("John Doe")
                .email("john@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        var savedDto = CustomerDto
                .builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        given(customerService.createCustomer(any())).willReturn(savedDto);

        // When and Then
        mockMvc.perform(post("/api/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/customers/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("John Doe")));
    }

    @Test
    void updateCustomer_successful() throws Exception {
        // Given
        var updateDto = UpdateCustomerDto
                .builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        var updatedDto = CustomerDto
                .builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        given(customerService.updateCustomer(any())).willReturn(Optional.of(updatedDto));

        // When and Then
        mockMvc.perform(put("/api/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCustomer_notFound() throws Exception {
        // Given
        var updateDto = UpdateCustomerDto
                .builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        given(customerService.updateCustomer(any())).willReturn(Optional.empty());

        // When and Then
        mockMvc.perform(put("/api/customers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCustomer_successful() throws Exception {
        // Given
        given(customerService.deleteCustomer(1L)).willReturn(true);

        // When and Then
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_notFound() throws Exception {
        // Given
        given(customerService.deleteCustomer(99L)).willReturn(false);

        // When and Then
        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerBookings_returnsBookingList() throws Exception {
        // Given
        var bookings = List.of(
                BookingDto.builder().id(1L).title("Booking A").build(),
                BookingDto.builder().id(2L).title("Booking B").build()
        );

        given(customerService.getCustomerBookings(1L)).willReturn(bookings);

        // When and Then
        mockMvc.perform(get("/api/customers/1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Booking A")))
                .andExpect(jsonPath("$[1].title", is("Booking B")));
    }
}
