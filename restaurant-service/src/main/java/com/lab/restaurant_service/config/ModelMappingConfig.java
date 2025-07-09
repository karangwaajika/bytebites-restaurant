package com.lab.restaurant_service.config;

import com.lab.restaurant_service.dto.MenuResponseDto;
import com.lab.restaurant_service.dto.RestaurantSummaryDto;
import com.lab.restaurant_service.model.MenuEntity;
import com.lab.restaurant_service.model.RestaurantEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMappingConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }
}
