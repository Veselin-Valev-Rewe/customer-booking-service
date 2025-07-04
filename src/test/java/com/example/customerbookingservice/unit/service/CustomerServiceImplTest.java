package com.example.customerbookingservice.unit.service;

import com.example.customerbookingservice.data.entity.Customer;
import com.example.customerbookingservice.data.enums.CustomerStatus;
import com.example.customerbookingservice.data.repository.BookingRepository;
import com.example.customerbookingservice.data.repository.CustomerRepository;
import com.example.customerbookingservice.dto.customer.CreateCustomerDto;
import com.example.customerbookingservice.dto.customer.UpdateCustomerDto;
import com.example.customerbookingservice.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private BookingRepository bookingRepository;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        bookingRepository = mock(BookingRepository.class);
        ModelMapper modelMapper = new ModelMapper();

        customerService = new CustomerServiceImpl(customerRepository, bookingRepository, modelMapper);
    }

    @Test
    void getCustomers_returnsMappedDtos() {
        // Given
        var customer = Customer.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .status(CustomerStatus.ACTIVE)
                .age(30)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        // When
        var customers = customerService.getCustomers();

        // Then
        assertThat(customers).hasSize(1);
        assertThat(customers.getFirst().getFullName()).isEqualTo("John Doe");
    }

    @Test
    void getCustomerById_found_returnsMappedDto() {
        // Given
        var customer = Customer.builder()
                .id(1L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .status(CustomerStatus.DEACTIVATED)
                .age(25)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        var result = customerService.getCustomerById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("Jane Doe");
    }

    @Test
    void getCustomerById_notFound_returnsEmpty() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        var result = customerService.getCustomerById(1L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void createCustomer_mapsAndSavesCorrectly() {
        // Given
        var dto = CreateCustomerDto.builder()
                .fullName("Alice")
                .email("alice@example.com")
                .status("ACTIVE")
                .age(40)
                .build();

        var now = LocalDateTime.now();

        var saved = Customer.builder()
                .id(1L)
                .fullName("Alice")
                .email("alice@example.com")
                .status(CustomerStatus.ACTIVE)
                .age(40)
                .created(now)
                .updated(now)
                .build();

        when(customerRepository.save(any())).thenReturn(saved);

        // When
        var result = customerService.createCustomer(dto);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullName()).isEqualTo("Alice");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_existingCustomer_updatedAndReturned() {
        // Given
        var now = LocalDateTime.now();
        var existing = Customer.builder()
                .id(1L)
                .fullName("Old Name")
                .email("old@example.com")
                .status(CustomerStatus.DEACTIVATED)
                .age(50)
                .created(now.minusDays(1))
                .updated(now.minusDays(1))
                .build();

        var updateDto = UpdateCustomerDto.builder()
                .id(1L)
                .fullName("New Name")
                .email("new@example.com")
                .status("ACTIVE")
                .age(45)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // When
        var result = customerService.updateCustomer(updateDto);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("New Name");
        assertThat(result.get().getStatus()).isEqualTo("ACTIVE");
        verify(customerRepository).save(existing);
    }

    @Test
    void updateCustomer_notFound_returnsEmpty() {
        // Given
        var updateDto = UpdateCustomerDto.builder()
                .id(999L)
                .fullName("Ghost")
                .email("ghost@example.com")
                .status("ACTIVE")
                .age(99)
                .build();

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        var result = customerService.updateCustomer(updateDto);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void deleteCustomer_exists_deletesAndReturnsTrue() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(true);

        // When
        var result = customerService.deleteCustomer(1L);

        // Then
        assertThat(result).isTrue();
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_doesNotExist_returnsFalse() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(false);

        // When
        var result = customerService.deleteCustomer(1L);

        // Then
        assertThat(result).isFalse();
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCustomerBookings_returnsMappedDtos() {
        // Given
        when(bookingRepository.findByCustomerId(1L)).thenReturn(emptyList());

        // When
        var bookings = customerService.getCustomerBookings(1L);

        // Then
        assertThat(bookings).isEmpty();
        verify(bookingRepository).findByCustomerId(1L);
    }
}
