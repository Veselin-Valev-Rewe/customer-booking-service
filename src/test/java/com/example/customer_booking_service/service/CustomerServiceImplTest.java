package com.example.customer_booking_service.service;

import com.example.customer_booking_service.data.entity.Customer;
import com.example.customer_booking_service.data.enums.CustomerStatus;
import com.example.customer_booking_service.data.repository.BookingRepository;
import com.example.customer_booking_service.data.repository.CustomerRepository;
import com.example.customer_booking_service.dto.booking.BookingDto;
import com.example.customer_booking_service.dto.customer.CreateCustomerDto;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import com.example.customer_booking_service.dto.customer.UpdateCustomerDto;
import com.example.customer_booking_service.service.impl.CustomerServiceImpl;
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
    private DateTimeService dateTimeService;
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        bookingRepository = mock(BookingRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        dateTimeService = mock(DateTimeService.class);

        customerService = new CustomerServiceImpl(customerRepository, bookingRepository, modelMapper, dateTimeService);
    }

    @Test
    void getCustomers_returnsMappedDtos() {
        Customer customer = Customer.builder()
                .id(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .status(CustomerStatus.ACTIVE)
                .age(30)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerDto> customers = customerService.getCustomers();

        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getFullName()).isEqualTo("John Doe");
    }

    @Test
    void getCustomerById_found_returnsMappedDto() {
        Customer customer = Customer.builder()
                .id(1L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .status(CustomerStatus.DEACTIVATED)
                .age(25)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<CustomerDto> result = customerService.getCustomerById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("Jane Doe");
    }

    @Test
    void getCustomerById_notFound_returnsEmpty() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.getCustomerById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void createCustomer_mapsAndSavesCorrectly() {
        CreateCustomerDto dto = CreateCustomerDto.builder()
                .fullName("Alice")
                .email("alice@example.com")
                .status("ACTIVE")
                .age(40)
                .build();

        LocalDateTime now = LocalDateTime.now();
        when(dateTimeService.now()).thenReturn(now);

        Customer saved = Customer.builder()
                .id(1L)
                .fullName("Alice")
                .email("alice@example.com")
                .status(CustomerStatus.ACTIVE)
                .age(40)
                .created(now)
                .updated(now)
                .build();

        when(customerRepository.save(any())).thenReturn(saved);

        CustomerDto result = customerService.createCustomer(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullName()).isEqualTo("Alice");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_existingCustomer_updatedAndReturned() {
        LocalDateTime now = LocalDateTime.now();
        Customer existing = Customer.builder()
                .id(1L)
                .fullName("Old Name")
                .email("old@example.com")
                .status(CustomerStatus.DEACTIVATED)
                .age(50)
                .created(now.minusDays(1))
                .updated(now.minusDays(1))
                .build();

        UpdateCustomerDto updateDto = UpdateCustomerDto.builder()
                .id(1L)
                .fullName("New Name")
                .email("new@example.com")
                .status("ACTIVE")
                .age(45)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(dateTimeService.now()).thenReturn(now);

        Optional<CustomerDto> result = customerService.updateCustomer(updateDto);

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("New Name");
        assertThat(result.get().getStatus()).isEqualTo("ACTIVE");
        verify(customerRepository).save(existing);
    }

    @Test
    void updateCustomer_notFound_returnsEmpty() {
        UpdateCustomerDto updateDto = UpdateCustomerDto.builder()
                .id(999L)
                .fullName("Ghost")
                .email("ghost@example.com")
                .status("ACTIVE")
                .age(99)
                .build();

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.updateCustomer(updateDto);

        assertThat(result).isEmpty();
    }

    @Test
    void deleteCustomer_exists_deletesAndReturnsTrue() {
        when(customerRepository.existsById(1L)).thenReturn(true);

        boolean result = customerService.deleteCustomer(1L);

        assertThat(result).isTrue();
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_doesNotExist_returnsFalse() {
        when(customerRepository.existsById(1L)).thenReturn(false);

        boolean result = customerService.deleteCustomer(1L);

        assertThat(result).isFalse();
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCustomerBookings_returnsMappedDtos() {
        when(bookingRepository.findByCustomerId(1L)).thenReturn(emptyList());

        List<BookingDto> bookings = customerService.getCustomerBookings(1L);

        assertThat(bookings).isEmpty();
        verify(bookingRepository).findByCustomerId(1L);
    }
}
