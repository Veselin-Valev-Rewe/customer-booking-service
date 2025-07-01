package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.entity.Brand;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.brand.BrandDto;
import com.example.customer_booking_service.dto.brand.CreateBrandDto;
import com.example.customer_booking_service.dto.brand.UpdateBrandDto;
import com.example.customer_booking_service.service.BrandService;
import com.example.customer_booking_service.service.DateTimeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final DateTimeService dateTimeService;

    @Override
    public List<BrandDto> getBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, BrandDto.class))
                .toList();
    }

    @Override
    public Optional<BrandDto> getBrandById(long id) {
        return brandRepository.findById(id)
                .map(brand -> modelMapper.map(brand, BrandDto.class));
    }

    @Override
    public BrandDto createBrand(CreateBrandDto brandDto) {
        var brand = modelMapper.map(brandDto, Brand.class);
        var dateNow = dateTimeService.now().toLocalDate();
        brand.setCreated(dateNow);
        brand.setUpdated(dateNow);

        return modelMapper.map(brandRepository.save(brand), BrandDto.class);
    }

    @Override
    public Optional<BrandDto> updateBrand(UpdateBrandDto brandDto) {
        return brandRepository.findById(brandDto.getId())
                .map(existingBrand -> {
                    existingBrand.setName(brandDto.getName());
                    existingBrand.setAddress(brandDto.getAddress());
                    existingBrand.setShortCode(brandDto.getShortCode());
                    existingBrand.setUpdated(dateTimeService.now().toLocalDate());

                    var savedBrand = brandRepository.save(existingBrand);
                    return modelMapper.map(savedBrand, BrandDto.class);
                });
    }

    @Override
    public boolean deleteBrand(long id) {
        if (!brandRepository.existsById(id)) {
            return false;
        }

        brandRepository.deleteById(id);
        return true;
    }

    @Override
    public List<BookingDto> getBrandBookings(long id) {
        return bookingRepository.findByBrandId(id)
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .toList();
    }
}
