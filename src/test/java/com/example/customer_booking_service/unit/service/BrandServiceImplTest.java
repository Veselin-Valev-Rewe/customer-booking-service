package com.example.customer_booking_service.unit.service;

import com.example.customer_booking_service.data.entity.Brand;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
import com.example.customer_booking_service.service.DateTimeService;
import com.example.customer_booking_service.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BrandServiceImplTest {

    private BrandRepository brandRepository;
    private BookingRepository bookingRepository;
    private DateTimeService dateTimeService;
    private BrandServiceImpl brandService;

    @BeforeEach
    void setUp() {
        brandRepository = mock(BrandRepository.class);
        bookingRepository = mock(BookingRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        dateTimeService = mock(DateTimeService.class);
        brandService = new BrandServiceImpl(brandRepository, bookingRepository, modelMapper, dateTimeService);
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

        when(brandRepository.findAll()).thenReturn(List.of(brand));

        // When
        var result = brandService.getBrands();

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
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Brand B");
    }

    @Test
    void getBrandById_notFound_returnsEmpty() {
        // Given
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        var result = brandService.getBrandById(999L);

        // Then
        assertThat(result).isEmpty();
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
        when(dateTimeService.now()).thenReturn(now);

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
        when(dateTimeService.now()).thenReturn(now);

        // When
        var result = brandService.updateBrand(updateDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Brand");
        assertThat(result.get().getShortCode()).isEqualTo("NEW");
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

        // When
        var result = brandService.updateBrand(dto);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void deleteBrand_exists_deletesAndReturnsTrue() {
        // Given
        when(brandRepository.existsById(10L)).thenReturn(true);

        // When
        var result = brandService.deleteBrand(10L);

        // Then
        assertThat(result).isTrue();
        verify(brandRepository).deleteById(10L);
    }

    @Test
    void deleteBrand_notFound_returnsFalse() {
        // Given
        when(brandRepository.existsById(11L)).thenReturn(false);

        // When
        var result = brandService.deleteBrand(11L);

        // Then
        assertThat(result).isFalse();
        verify(brandRepository, never()).deleteById(anyLong());
    }

    @Test
    void getBrandBookings_returnsMappedDtos() {
        // Given
        when(bookingRepository.findByBrandId(1L)).thenReturn(emptyList());

        // When
        var bookings = brandService.getBrandBookings(1L);

        // Then
        assertThat(bookings).isEmpty();
        verify(bookingRepository).findByBrandId(1L);
    }
}
