package com.example.customer_booking_service.service.impl;

import com.example.customer_booking_service.data.repository.BrandRepository;
import com.example.customer_booking_service.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
}
