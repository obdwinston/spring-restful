package com.example.database.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    // The ModelMapper type is from the Model Mapper library.
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
