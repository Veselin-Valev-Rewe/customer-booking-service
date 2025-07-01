package com.example.customer_booking_service.config.modelmaper;

import com.example.customer_booking_service.data.entity.Customer;
import com.example.customer_booking_service.dto.customer.CustomerDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();
        modelMapper.typeMap(Customer.class, CustomerDto.class)
            .addMappings(mapper -> mapper.map(
                    src -> src.getStatus() != null ? src.getStatus().name() : null,
                    CustomerDto::setStatus
            ));

        return modelMapper;
    }
}
