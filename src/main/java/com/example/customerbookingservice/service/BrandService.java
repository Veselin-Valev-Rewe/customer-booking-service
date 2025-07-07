package com.example.customerbookingservice.service;

import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.brand.BrandDto;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandService {
    List<BrandDto> getBrands(Pageable pageable);

    BrandDto getBrandById(long id);

    BrandDto createBrand(@Valid CreateBrandDto brandDto);

    BrandDto updateBrand(@Valid UpdateBrandDto brandDto);

    void deleteBrand(long id);

    List<BookingDto> getBrandBookings(long id, Pageable pageable);
}
