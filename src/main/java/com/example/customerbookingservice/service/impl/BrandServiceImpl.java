package com.example.customerbookingservice.service.impl;

import com.example.customerbookingservice.data.entity.Brand;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.BrandRepository;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.brand.BrandDto;
import com.example.customerbookingservice.dto.brand.CreateBrandDto;
import com.example.customerbookingservice.dto.brand.UpdateBrandDto;
import com.example.customerbookingservice.service.BrandService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BrandDto> getBrands(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .stream()
                .map(brand -> modelMapper.map(brand, BrandDto.class))
                .toList();
    }

    @Override
    public BrandDto getBrandById(long id) {
        var brand = getBrand(id);

        return modelMapper.map(brand, BrandDto.class);
    }

    @Override
    public BrandDto createBrand(CreateBrandDto brandDto) {
        var brand = modelMapper.map(brandDto, Brand.class);
        return modelMapper.map(brandRepository.save(brand), BrandDto.class);
    }

    @Override
    public BrandDto updateBrand(UpdateBrandDto brandDto) {
        var existingBrand = getBrand(brandDto.getId());
        existingBrand.setName(brandDto.getName());
        existingBrand.setAddress(brandDto.getAddress());
        existingBrand.setShortCode(brandDto.getShortCode());

        var savedBrand = brandRepository.save(existingBrand);
        return modelMapper.map(savedBrand, BrandDto.class);
    }

    @Override
    public void deleteBrand(long id) {
        if (!brandRepository.existsById(id)) {
            throw new EntityNotFoundException("Brand not found");
        }

        brandRepository.deleteById(id);
    }

    @Override
    public List<BookingDto> getBrandBookings(long id, Pageable pageable) {
        return bookingRepository.findByBrandId(id, pageable)
                .stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .toList();
    }

    private Brand getBrand(long id) {
        var brandOptional = brandRepository.findById(id);
        if (brandOptional.isEmpty()) {
            throw new EntityNotFoundException("Brand not found");
        }
        return brandOptional.get();
    }
}
