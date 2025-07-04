package com.example.customerbookingservice.service;

import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.brand.BrandDto;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
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
