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
@RequestMapping("/api/brand")
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/all")
    public ResponseEntity<List<BrandDto>> getBrands() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/get")
    public ResponseEntity<BrandDto> getBrandById(@RequestParam long id) {
        return ResponseEntity.ok(BrandDto.builder().build());
    }

    @PostMapping("/create")
    public ResponseEntity<BrandDto> createBrand(@RequestBody @Valid CreateBrandDto brandDto) {
        var createdBrand = BrandDto.builder().build();
        URI location = URI.create("/brand/" + createdBrand.getId());
        return ResponseEntity.created(location).body(createdBrand);
    }

    @PutMapping("/update")
    public ResponseEntity<BrandDto> updateBrand(@RequestBody @Valid UpdateBrandDto brandDto) {
        return ResponseEntity.ok(BrandDto.builder().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBrand(@RequestParam long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDto>> getBrandBookings(@RequestParam long id) {
        return ResponseEntity.ok(List.of());
    }
}
