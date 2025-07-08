package com.lab.restaurant_service.service;

import com.lab.restaurant_service.dto.RestaurantRequestDto;
import com.lab.restaurant_service.dto.RestaurantResponseDto;
import com.lab.restaurant_service.model.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantService {
    RestaurantResponseDto create(RestaurantRequestDto userDto);
    Optional<RestaurantEntity> findByName(String name);
    Page<RestaurantResponseDto> findAll(Pageable pageable);
}
