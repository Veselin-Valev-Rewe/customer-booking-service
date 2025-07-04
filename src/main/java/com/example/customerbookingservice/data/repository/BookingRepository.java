package com.example.customerbookingservice.data.repository;

import com.example.customerbookingservice.data.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(long customerId);

    List<Booking> findByBrandId(long brandId);
}
