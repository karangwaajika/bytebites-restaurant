package com.lab.auth_service.service;

import com.lab.auth_service.dto.UserRegisterDto;
import com.lab.auth_service.dto.UserResponseDto;
import com.lab.auth_service.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    UserResponseDto create(UserRegisterDto userDto);
    Optional<UserEntity> findByEmail(String email);
    Page<UserResponseDto> findAll(Pageable pageable);
    Optional<UserEntity> findById(Long userId);

}
