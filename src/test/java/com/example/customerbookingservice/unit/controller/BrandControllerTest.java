package com.example.customerbookingservice.unit.controller;

import com.example.customerbookingservice.controller.BrandController;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.brand.BrandDto;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
import com.example.customerbookingservice.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BrandService brandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBrands_returnsList() throws Exception {
        // Given
        var brands = List.of(
                BrandDto.builder().id(1L).name("Brand A").address("Address A").shortCode("A1").build(),
                BrandDto.builder().id(2L).name("Brand B").address("Address B").shortCode("B2").build()
        );

        given(brandService.getBrands(any(Pageable.class))).willReturn(brands);

        // When and Then
        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Brand A")))
                .andExpect(jsonPath("$[1].shortCode", is("B2")));
    }

    @Test
    void getBrandById_found() throws Exception {
        // Given
        var dto = BrandDto.builder()
                .id(1L)
                .name("Brand A")
                .address("Address A")
                .shortCode("A1")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        given(brandService.getBrandById(1L)).willReturn(dto);

        // When and Then
        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Brand A")))
                .andExpect(jsonPath("$.shortCode", is("A1")));
    }

    @Test
    void createBrand_returnsCreatedBrand() throws Exception {
        // Given
        var createDto = CreateBrandDto.builder()
                .name("New Brand")
                .address("New Address")
                .shortCode("NB")
                .build();

        var savedDto = BrandDto.builder()
                .id(1L)
                .name("New Brand")
                .address("New Address")
                .shortCode("NB")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        given(brandService.createBrand(any())).willReturn(savedDto);

        // When and Then
        mockMvc.perform(post("/api/brands")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/brands/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Brand")));
    }

    @Test
    void updateBrand_successful() throws Exception {
        // Given
        var updateDto = UpdateBrandDto.builder()
                .id(1L)
                .name("Updated Brand")
                .address("Updated Address")
                .shortCode("UB")
                .build();

        var updatedDto = BrandDto.builder()
                .id(1L)
                .name("Updated Brand")
                .address("Updated Address")
                .shortCode("UB")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        given(brandService.updateBrand(any())).willReturn(updatedDto);

        // When and Then
        mockMvc.perform(put("/api/brands")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBrand_successful() throws Exception {
        // When and Then
        mockMvc.perform(delete("/api/brands/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBrandBookings_returnsList() throws Exception {
        // Given
        var bookings = List.of(
                BookingDto.builder().id(1L).title("Booking 1").build(),
                BookingDto.builder().id(2L).title("Booking 2").build()
        );

        given(brandService.getBrandBookings(eq(1L), any(Pageable.class))).willReturn(bookings);

        // When and Then
        mockMvc.perform(get("/api/brands/1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].title", is("Booking 1")))
                .andExpect(jsonPath("$[1].title", is("Booking 2")));
    }
}
