package com.example.customerbookingservice.controller;

import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.brand.BrandDto;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
import com.example.customerbookingservice.service.BrandService;
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
        return ResponseEntity.ok(brandService.getBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable long id) {
        var brand = brandService.getBrandById(id);
        return brand.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<BrandDto> createBrand(@RequestBody @Valid CreateBrandDto brandDto) {
        var brand = brandService.createBrand(brandDto);
        URI location = URI.create("/api/brands/" + brand.getId());
        return ResponseEntity.created(location).body(brand);
    }

    @PutMapping()
    public ResponseEntity<BrandDto> updateBrand(@RequestBody @Valid UpdateBrandDto brandDto) {
        var brand = brandService.updateBrand(brandDto);
        return brand.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable long id) {
        return brandService.deleteBrand(id) ?
                ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingDto>> getBrandBookings(@PathVariable long id) {
        return ResponseEntity.ok(brandService.getBrandBookings(id));
    }
}
