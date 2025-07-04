package com.example.customerbookingservice.integration.controller;

import com.example.customerbookingservice.data.entity.Customer;
import com.example.customerbookingservice.data.enums.CustomerStatus;
import com.example.customerbookingservice.data.repository.CustomerRepository;
import com.example.customerbookingservice.dto.booking.BookingDto;
import com.example.customerbookingservice.dto.customer.CreateCustomerDto;
import com.example.customerbookingservice.dto.customer.CustomerDto;
import com.example.customerbookingservice.dto.customer.UpdateCustomerDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerControllerIntegrationTest extends AbstractDbIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    public void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldReturnCustomersList() {
        // Given
        var customerList = List.of(
                Customer.builder().fullName("John Doe").email("JohnDoe@gmail.com").build(),
                Customer.builder().fullName("Jane Smith").email("JaneSmith@gmail.com").build()
        );
        customerRepository.saveAll(customerList);

        // When
        var response = restTemplate.exchange(
                "/api/customers",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CustomerDto>>() {
                }
        );

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting(CustomerDto::getFullName)
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    void shouldGetCustomerById() {
        // Given
        var customer = Customer.builder()
                .fullName("John Doe")
                .email("JohnDoe@gmail.com")
                .build();
        customer = customerRepository.save(customer);

        // When
        var response = restTemplate.getForEntity("/api/customers/{id}", CustomerDto.class, customer.getId());

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFullName()).isEqualTo("John Doe");
        assertThat(response.getBody().getEmail()).isEqualTo("JohnDoe@gmail.com");
    }

    @Test
    void shouldReturnNotFound_ForNonExistingCustomerById() {
        // When
        var response = restTemplate.getForEntity("/api/customers/{id}", CustomerDto.class, 9999L);

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldCreateCustomer() {
        // Given
        var createDto = CreateCustomerDto.builder()
                .fullName("Alice Wonderland")
                .email("alice@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/customers",
                HttpMethod.POST,
                new HttpEntity<>(createDto),
                new ParameterizedTypeReference<CustomerDto>() {
                });

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFullName()).isEqualTo("Alice Wonderland");
        assertThat(response.getBody().getEmail()).isEqualTo("alice@example.com");

        var savedCustomer = customerRepository.findById(response.getBody().getId());
        assertThat(savedCustomer).isPresent();
        assertThat(savedCustomer.get().getFullName()).isEqualTo("Alice Wonderland");
    }

    @Test
    void shouldUpdateCustomer() {
        // Given
        var existingCustomer = Customer.builder()
                .fullName("Old Name")
                .email("oldemail@example.com")
                .status(CustomerStatus.ACTIVE)
                .age(26)
                .build();
        existingCustomer = customerRepository.save(existingCustomer);

        var updateDto = UpdateCustomerDto
                .builder()
                .id(existingCustomer.getId())
                .fullName("New Name")
                .email("newemail@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/customers",
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                new ParameterizedTypeReference<CustomerDto>() {
                });

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFullName()).isEqualTo("New Name");
        assertThat(response.getBody().getEmail()).isEqualTo("newemail@example.com");

        var updatedCustomer = customerRepository.findById(existingCustomer.getId()).orElseThrow();
        assertThat(updatedCustomer.getFullName()).isEqualTo("New Name");
    }

    @Test
    void shouldReturnNotFoundOnUpdate_ForNonExistingCustomer() {
        // Given
        var updateDto = UpdateCustomerDto
                .builder()
                .id(999L)
                .fullName("New Name")
                .email("newemail@example.com")
                .status("ACTIVE")
                .age(27)
                .build();

        // When
        var response = restTemplate.exchange(
                "/api/customers",
                HttpMethod.PUT,
                new HttpEntity<>(updateDto),
                new ParameterizedTypeReference<CustomerDto>() {
                });

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldDeleteCustomer() {
        // Given
        var customer = Customer.builder()
                .fullName("To Delete")
                .email("todelete@example.com")
                .build();
        customer = customerRepository.save(customer);

        // When
        var response = restTemplate.exchange(
                "/api/customers/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                customer.getId());

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(customerRepository.existsById(customer.getId())).isFalse();
    }

    @Test
    void shouldReturnNotFoundOnDelete_ForNonExistingCustomer() {
        // When
        var response = restTemplate.exchange(
                "/api/customers/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                9999L);

        // Then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldReturnCustomerBookings() {
        // Given
        var customer = Customer.builder()
                .fullName("Booking Customer")
                .email("booking@example.com")
                .build();
        customer = customerRepository.save(customer);

        // When
        var response = restTemplate.exchange(
                "/api/customers/{id}/bookings",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookingDto>>() {
                },
                customer.getId());

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }
}
