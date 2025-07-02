package com.example.customer_booking_service.service;

import com.example.customer_booking_service.data.entity.Brand;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.brand.BrandDto;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
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
        Brand brand = Brand.builder()
                .id(1L)
                .name("Brand A")
                .address("Address A")
                .shortCode("BR-A")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(brandRepository.findAll()).thenReturn(List.of(brand));

        List<BrandDto> result = brandService.getBrands();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Brand A");
    }

    @Test
    void getBrandById_found_returnsMappedDto() {
        Brand brand = Brand.builder()
                .id(2L)
                .name("Brand B")
                .address("Address B")
                .shortCode("BR-B")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(brandRepository.findById(2L)).thenReturn(Optional.of(brand));

        Optional<BrandDto> result = brandService.getBrandById(2L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Brand B");
    }

    @Test
    void getBrandById_notFound_returnsEmpty() {
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<BrandDto> result = brandService.getBrandById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void createBrand_mapsAndSavesCorrectly() {
        CreateBrandDto createDto = CreateBrandDto.builder()
                .name("Brand C")
                .address("Address C")
                .shortCode("BR-C")
                .build();

        LocalDateTime now = LocalDateTime.now();
        when(dateTimeService.now()).thenReturn(now);

        Brand savedBrand = Brand.builder()
                .id(3L)
                .name("Brand C")
                .address("Address C")
                .shortCode("BR-C")
                .created(now)
                .updated(now)
                .build();

        when(brandRepository.save(any())).thenReturn(savedBrand);

        BrandDto result = brandService.createBrand(createDto);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Brand C");
        verify(brandRepository).save(any(Brand.class));
    }

    @Test
    void updateBrand_existingBrand_updatedAndReturned() {
        LocalDateTime now = LocalDateTime.now();

        Brand existing = Brand.builder()
                .id(4L)
                .name("Old Brand")
                .address("Old Address")
                .shortCode("OLD")
                .created(now.minusDays(1))
                .updated(now.minusDays(1))
                .build();

        UpdateBrandDto updateDto = UpdateBrandDto.builder()
                .id(4L)
                .name("Updated Brand")
                .address("New Address")
                .shortCode("NEW")
                .build();

        when(brandRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(brandRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(dateTimeService.now()).thenReturn(now);

        Optional<BrandDto> result = brandService.updateBrand(updateDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Brand");
        assertThat(result.get().getShortCode()).isEqualTo("NEW");
        verify(brandRepository).save(existing);
    }

    @Test
    void updateBrand_notFound_returnsEmpty() {
        UpdateBrandDto dto = UpdateBrandDto.builder()
                .id(404L)
                .name("Ghost Brand")
                .address("Nowhere")
                .shortCode("GHOST")
                .build();

        when(brandRepository.findById(404L)).thenReturn(Optional.empty());

        Optional<BrandDto> result = brandService.updateBrand(dto);

        assertThat(result).isEmpty();
    }

    @Test
    void deleteBrand_exists_deletesAndReturnsTrue() {
        when(brandRepository.existsById(10L)).thenReturn(true);

        boolean result = brandService.deleteBrand(10L);

        assertThat(result).isTrue();
        verify(brandRepository).deleteById(10L);
    }

    @Test
    void deleteBrand_notFound_returnsFalse() {
        when(brandRepository.existsById(11L)).thenReturn(false);

        boolean result = brandService.deleteBrand(11L);

        assertThat(result).isFalse();
        verify(brandRepository, never()).deleteById(anyLong());
    }

    @Test
    void getBrandBookings_returnsMappedDtos() {
        when(bookingRepository.findByBrandId(1L)).thenReturn(emptyList());

        List<BookingDto> bookings = brandService.getBrandBookings(1L);

        assertThat(bookings).isEmpty();
        verify(bookingRepository).findByBrandId(1L);
    }
}
