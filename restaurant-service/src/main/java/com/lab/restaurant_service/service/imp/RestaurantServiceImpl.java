package com.lab.restaurant_service.service.imp;

import com.lab.restaurant_service.dto.RestaurantRequestDto;
import com.lab.restaurant_service.dto.RestaurantResponseDto;
import com.lab.restaurant_service.exception.RestaurantExistsException;
import com.lab.restaurant_service.model.RestaurantEntity;
import com.lab.restaurant_service.repository.RestaurantRepository;
import com.lab.restaurant_service.service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    ModelMapper modelMapper;
    RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(ModelMapper modelMapper,
                                 RestaurantRepository restaurantRepository) {
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantResponseDto create(RestaurantRequestDto userDto) {
        if(findByName(userDto.getName()).isPresent()){
            throw new RestaurantExistsException(
                    String.format("A restaurant with the email '%s' already exist",
                            userDto.getName()));
        }
        RestaurantEntity user = this.modelMapper.map(userDto, RestaurantEntity.class);
        RestaurantEntity savedUser = this.restaurantRepository.save(user);

        return this.modelMapper.map(savedUser, RestaurantResponseDto.class);
    }

    @Override
    public Optional<RestaurantEntity> findByName(String name) {
        return this.restaurantRepository.findByName(name);
    }

    @Override
    public Page<RestaurantResponseDto> findAll(Pageable pageable) {
        Page<RestaurantEntity> restaurants = this.restaurantRepository.findAll(pageable);
        return restaurants.map(restaurant->this.modelMapper.map(restaurant, RestaurantResponseDto.class));
    }
}
