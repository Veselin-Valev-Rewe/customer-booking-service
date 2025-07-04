package com.example.customerbookingservice.data.entity;

import com.example.customerbookingservice.data.enums.CustomerStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends BaseEntity {
    private String fullName;

    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    private int age;

    @OneToMany(mappedBy = "customer")
    private Set<Booking> bookings;
}
