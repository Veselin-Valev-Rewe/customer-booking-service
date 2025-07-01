package com.example.customer_booking_service.service;

import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.brand.BrandDto;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<BrandDto> getBrands();

    Optional<BrandDto> getBrandById(long id);

    BrandDto createBrand(@Valid CreateBrandDto brandDto);

    Optional<BrandDto> updateBrand(@Valid UpdateBrandDto brandDto);

    boolean deleteBrand(long id);

    List<BookingDto> getBrandBookings(long id);
}
