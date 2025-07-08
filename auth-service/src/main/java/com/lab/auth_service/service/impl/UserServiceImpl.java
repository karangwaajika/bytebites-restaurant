package com.lab.auth_service.service.impl;

import com.lab.auth_service.dto.UserRegisterDto;
import com.lab.auth_service.dto.UserResponseDto;
import com.lab.auth_service.exception.UserExistsException;
import com.lab.auth_service.model.UserEntity;
import com.lab.auth_service.repository.UserRepository;
import com.lab.auth_service.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto create(UserRegisterDto userDto) {
        if(findByEmail(userDto.getEmail()).isPresent()){
            throw new UserExistsException(
                    String.format("A user with the email '%s' already exist",
                            userDto.getEmail()));
        }
        // hash the password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity user = this.modelMapper.map(userDto, UserEntity.class);
        UserEntity savedUser = this.userRepository.save(user);

        return this.modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        Page<UserEntity> users = this.userRepository.findAll(pageable);
        return users.map(user->this.modelMapper.map(user, UserResponseDto.class));
    }
}
