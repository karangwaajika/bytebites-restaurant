package com.lab.restaurant_service.service;

import com.lab.restaurant_service.dto.RestaurantRequestDto;
import com.lab.restaurant_service.dto.RestaurantResponseDto;
import com.lab.restaurant_service.model.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

public interface RestaurantService {
    RestaurantResponseDto create(RestaurantRequestDto restaurantRequestDto) throws Exception;
    Optional<RestaurantEntity> findByName(String name);
    Optional<RestaurantEntity> findById(Long id);
    Page<RestaurantResponseDto> findAll(Pageable pageable);
    RestaurantResponseDto partialUpdate(RestaurantRequestDto restaurantRequestDto, Long restaurantId);
    void deleteById(Long id);
}
