package com.example.customer_booking_service.controller;

import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.brand.BrandDto;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
import com.example.customer_booking_service.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;

    @GetMapping()
    public ResponseEntity<List<BrandDto>> getBrands() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable long id) {
        return ResponseEntity.ok(BrandDto.builder().build());
    }

    @PostMapping()
    public ResponseEntity<BrandDto> createBrand(@RequestBody @Valid CreateBrandDto brandDto) {
        var createdBrand = BrandDto.builder().build();
        URI location = URI.create("/brands/" + createdBrand.getId());
        return ResponseEntity.created(location).body(createdBrand);
    }

    @PutMapping()
    public ResponseEntity<BrandDto> updateBrand(@RequestBody @Valid UpdateBrandDto brandDto) {
        return ResponseEntity.ok(BrandDto.builder().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getBrandBookings(@PathVariable long id) {
        return ResponseEntity.ok(List.of());
    }
}
