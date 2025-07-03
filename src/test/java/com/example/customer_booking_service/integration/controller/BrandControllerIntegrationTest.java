package com.example.customer_booking_service.integration.controller;

import com.example.customer_booking_service.data.entity.Brand;
import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.brand.BrandDto;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BrandControllerIntegrationTest extends AbstractDbIntegrationTest {

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        brandRepository.deleteAll();
    }

    @Test
    void shouldReturnBrandsList() {
        // Given
        var brandList = List.of(
                Brand.builder().name("Brand A").address("Address A").shortCode("BA").build(),
                Brand.builder().name("Brand B").address("Address B").shortCode("BB").build()
        );
        brandRepository.saveAll(brandList);

        // When
        var response = restTemplate.exchange(
                "/api/brands",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BrandDto>>() {
                }
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting(BrandDto::getName)
                .containsExactlyInAnyOrder("Brand A", "Brand B");
    }

    @Test
    void shouldGetBrandById() {
        // Given
        var brand = Brand.builder()
                .name("Brand X")
                .address("Some Address")
                .shortCode("BX")
                .build();
        brand = brandRepository.save(brand);

        // When
        var response = restTemplate.getForEntity("/api/brands/{id}", BrandDto.class, brand.getId());

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Brand X");
        assertThat(response.getBody().getAddress()).isEqualTo("Some Address");
    }

    @Test
    void shouldReturnNotFound_ForNonExistingBrandById() {
        // When
        var response = restTemplate.getForEntity("/api/brands/{id}", BrandDto.class, 9999L);

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldCreateBrand() {
        // Given
        var createDto = CreateBrandDto.builder()
                .name("New Brand")
                .address("New Address")
                .shortCode("NB")
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/brands",
                HttpMethod.POST,
                new HttpEntity<>(createDto),
                new ParameterizedTypeReference<BrandDto>() {
                }
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Brand");

        var savedBrand = brandRepository.findById(response.getBody().getId());
        assertThat(savedBrand).isPresent();
        assertThat(savedBrand.get().getName()).isEqualTo("New Brand");
    }

    @Test
    void shouldUpdateBrand() {
        // Given
        var existingBrand = Brand.builder()
                .name("Old Brand")
                .address("Old Address")
                .shortCode("OB")
                .build();
        existingBrand = brandRepository.save(existingBrand);

        var updateDto = UpdateBrandDto.builder()
                .id(existingBrand.getId())
                .name("Updated Brand")
                .address("Updated Address")
                .shortCode("UB")
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/brands",
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                new ParameterizedTypeReference<BrandDto>() {
                }
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Brand");

        var updatedBrand = brandRepository.findById(existingBrand.getId()).orElseThrow();
        assertThat(updatedBrand.getName()).isEqualTo("Updated Brand");
    }

    @Test
    void shouldReturnNotFoundOnUpdate_ForNonExistingBrand() {
        // Given
        var updateDto = UpdateBrandDto.builder()
                .id(999L)
                .name("Non-Existent Brand")
                .address("Nowhere")
                .shortCode("NE")
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/brands",
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                new ParameterizedTypeReference<BrandDto>() {
                }
        );

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldDeleteBrand() {
        // Given
        var brand = Brand.builder()
                .name("Delete Brand")
                .address("Delete Address")
                .shortCode("DB")
                .build();
        brand = brandRepository.save(brand);

        // When
        var response = restTemplate.exchange(
                "/api/brands/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                brand.getId()
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(brandRepository.existsById(brand.getId())).isFalse();
    }

    @Test
    void shouldReturnNotFoundOnDelete_ForNonExistingBrand() {
        // When
        var response = restTemplate.exchange(
                "/api/brands/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                9999L
        );

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldReturnBrandBookings() {
        // Given
        var brand = Brand.builder()
                .name("Booking Brand")
                .address("Booking Address")
                .shortCode("BB")
                .build();
        brand = brandRepository.save(brand);

        // When
        var response = restTemplate.exchange(
                "/api/brands/{id}/bookings",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookingDto>>() {
                },
                brand.getId()
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }
}
