package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.brand.BrandDto;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
import com.example.customer_booking_service.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public List<BrandDto> getBrands() {
        return List.of();
    }

    @Override
    public Optional<BrandDto> getBrandById(long id) {
        return Optional.empty();
    }

    @Override
    public BrandDto createBrand(CreateBrandDto brandDto) {
        return null;
    }

    @Override
    public Optional<BrandDto> updateBrand(UpdateBrandDto brandDto) {
        return Optional.empty();
    }

    @Override
    public boolean deleteBrand(long id) {
        return false;
    }

    @Override
    public List<BookingDto> getBrandBookings(long id) {
        return List.of();
    }
}
