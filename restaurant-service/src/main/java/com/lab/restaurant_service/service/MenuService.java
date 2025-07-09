package com.lab.restaurant_service.service;

import com.lab.restaurant_service.dto.MenuRequestDto;
import com.lab.restaurant_service.dto.MenuResponseDto;
import com.lab.restaurant_service.model.MenuEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MenuService {
    MenuResponseDto create(MenuRequestDto menuRequestDto);
    Optional<MenuEntity> findByName(String name);
    Optional<MenuEntity> findById(Long id);
    Page<MenuResponseDto> findAll(Pageable pageable);
    MenuResponseDto partialUpdate(MenuRequestDto menuRequestDto, Long menuId);
    void deleteById(Long id);
}
