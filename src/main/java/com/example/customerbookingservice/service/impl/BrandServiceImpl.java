package com.example.customerbookingservice.service.impl;

import com.example.customerbookingservice.data.entity.Brand;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.BrandRepository;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.brand.BrandDto;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
import com.example.customerbookingservice.service.BrandService;
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
        return modelMapper.map(brandRepository.save(brand), BrandDto.class);
    }

    @Override
    public Optional<BrandDto> updateBrand(UpdateBrandDto brandDto) {
        return brandRepository.findById(brandDto.getId())
                .map(existingBrand -> {
                    existingBrand.setName(brandDto.getName());
                    existingBrand.setAddress(brandDto.getAddress());
                    existingBrand.setShortCode(brandDto.getShortCode());

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
