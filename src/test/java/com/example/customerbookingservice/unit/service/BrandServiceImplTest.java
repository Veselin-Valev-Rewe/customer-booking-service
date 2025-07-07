package com.example.customerbookingservice.unit.service;

import com.example.customerbookingservice.data.entity.Brand;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.BrandRepository;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
import com.example.customerbookingservice.service.impl.BrandServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BrandServiceImplTest {

    private BrandRepository brandRepository;
    private BookingRepository bookingRepository;
    private BrandServiceImpl brandService;

    @BeforeEach
    void setUp() {
        brandRepository = mock(BrandRepository.class);
        bookingRepository = mock(BookingRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        brandService = new BrandServiceImpl(brandRepository, bookingRepository, modelMapper);
    }

    @Test
    void getBrands_returnsMappedDtos() {
        // GIven
        var brand = Brand.builder()
                .id(1L)
                .name("Brand A")
                .address("Address A")
                .shortCode("BR-A")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(brandRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(brand)));

        // When
        var result = brandService.getBrands(PageRequest.of(0, 10));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Brand A");
    }

    @Test
    void getBrandById_found_returnsMappedDto() {
        // Given
        var brand = Brand.builder()
                .id(2L)
                .name("Brand B")
                .address("Address B")
                .shortCode("BR-B")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(brandRepository.findById(2L)).thenReturn(Optional.of(brand));

        // When
        var result = brandService.getBrandById(2L);

        // Then
        assertThat(result.getName()).isEqualTo("Brand B");
    }

    @Test
    void getBrandById_notFound_returnsEmpty() {
        // Given
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        // When + Then
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> brandService.getBrandById(999L)
        );

        assertEquals("Brand not found", exception.getMessage());
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    @Test
    void createBrand_mapsAndSavesCorrectly() {
        // Given
        var createDto = CreateBrandDto.builder()
                .name("Brand C")
                .address("Address C")
                .shortCode("BR-C")
                .build();

        var now = LocalDateTime.now();

        var savedBrand = Brand.builder()
                .id(3L)
                .name("Brand C")
                .address("Address C")
                .shortCode("BR-C")
                .created(now)
                .updated(now)
                .build();

        when(brandRepository.save(any())).thenReturn(savedBrand);

        // When
        var result = brandService.createBrand(createDto);

        // Then
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Brand C");
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void updateBrand_existingBrand_updatedAndReturned() {
        // Given
        var now = LocalDateTime.now();

        var existing = Brand.builder()
                .id(4L)
                .name("Old Brand")
                .address("Old Address")
                .shortCode("OLD")
                .created(now.minusDays(1))
                .updated(now.minusDays(1))
                .build();

        var updateDto = UpdateBrandDto.builder()
                .id(4L)
                .name("Updated Brand")
                .address("New Address")
                .shortCode("NEW")
                .build();

        when(brandRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(brandRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        var result = brandService.updateBrand(updateDto);

        // Then
        assertThat(result.getName()).isEqualTo("Updated Brand");
        assertThat(result.getShortCode()).isEqualTo("NEW");
        verify(brandRepository).save(existing);
    }

    @Test
    void updateBrand_notFound_returnsEmpty() {
        // Given
        var dto = UpdateBrandDto.builder()
                .id(404L)
                .name("Ghost Brand")
                .address("Nowhere")
                .shortCode("GHOST")
                .build();

        when(brandRepository.findById(404L)).thenReturn(Optional.empty());

        // When + Then
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> brandService.updateBrand(dto)
        );

        assertEquals("Brand not found", exception.getMessage());
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteBrand_exists_deletesAndReturnsTrue() {
        // Given
        when(brandRepository.existsById(10L)).thenReturn(true);

        // When
        brandService.deleteBrand(10L);

        // Then
        verify(brandRepository).deleteById(10L);
    }

    @Test
    void deleteBrand_notFound_returnsFalse() {
        // Given
        when(brandRepository.existsById(11L)).thenReturn(false);

        // When + Then
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> brandService.deleteBrand(11L)
        );

        assertEquals("Brand not found", exception.getMessage());
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    @Test
    void getBrandBookings_returnsMappedDtos() {
        // Given
        when(bookingRepository.findByBrandId(eq(1L), any(Pageable.class))).thenReturn(emptyList());

        // When
        var bookings = brandService.getBrandBookings(1L, PageRequest.of(0, 10));

        // Then
        assertThat(bookings).isEmpty();
        verify(bookingRepository).findByBrandId(eq(1L), any(Pageable.class));
    }
}
