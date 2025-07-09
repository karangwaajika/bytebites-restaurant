package com.lab.restaurant_service.service.imp;

import com.lab.restaurant_service.dto.RestaurantRequestDto;
import com.lab.restaurant_service.dto.RestaurantResponseDto;
import com.lab.restaurant_service.exception.RestaurantExistsException;
import com.lab.restaurant_service.exception.RestaurantNotFoundException;
import com.lab.restaurant_service.exception.UserNotFoundException;
import com.lab.restaurant_service.model.RestaurantEntity;
import com.lab.restaurant_service.repository.RestaurantRepository;
import com.lab.restaurant_service.service.AuthServiceUser;
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
    AuthServiceUser authServiceUser;

    public RestaurantServiceImpl(ModelMapper modelMapper,
                                 RestaurantRepository restaurantRepository,
                                 AuthServiceUser authServiceUser) {
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
        this.authServiceUser = authServiceUser;
    }

    @Override
    public RestaurantResponseDto create(RestaurantRequestDto restaurantDto) {
        if(findByName(restaurantDto.getName()).isPresent()){
            throw new RestaurantExistsException(
                    String.format("A restaurant with the name '%s' already exist",
                            restaurantDto.getName()));
        }

        if (!authServiceUser.isUserExists(restaurantDto.getOwnerId())) {
            throw new UserNotFoundException(
                    String.format("Restaurant owner with id '%s' doesn't exist", restaurantDto.getOwnerId()));
        }

        RestaurantEntity restaurant = this.modelMapper.map(restaurantDto, RestaurantEntity.class);
        restaurant.setId(null);
        RestaurantEntity savedRestaurant = this.restaurantRepository.save(restaurant);

        return this.modelMapper.map(savedRestaurant, RestaurantResponseDto.class);
    }

    @Override
    public Optional<RestaurantEntity> findByName(String name) {
        return this.restaurantRepository.findByName(name);
    }

    @Override
    public Optional<RestaurantEntity> findById(Long id) {
        return this.restaurantRepository.findById(id);
    }

    @Override
    public Page<RestaurantResponseDto> findAll(Pageable pageable) {
        Page<RestaurantEntity> restaurants = this.restaurantRepository.findAll(pageable);
        return restaurants.map(restaurant->this.modelMapper.map(restaurant, RestaurantResponseDto.class));
    }

    @Override
    public RestaurantResponseDto partialUpdate(RestaurantRequestDto restaurantRequestDto, Long restaurantId) {
        RestaurantEntity restaurantEntity = findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(
                        String.format("A restaurant with the Id '%d' doesn't exist", restaurantId)));

        if(restaurantRequestDto.getName() != null){
            restaurantEntity.setName(restaurantRequestDto.getName());
        }
        if(restaurantRequestDto.getLocation() != null){
            restaurantEntity.setLocation(restaurantRequestDto.getLocation());
        }
        if(restaurantRequestDto.getType() != null){
            restaurantEntity.setType(restaurantRequestDto.getType());
        }

        RestaurantEntity updatedRestaurant = this.restaurantRepository.save(restaurantEntity);

        return this.modelMapper.map(updatedRestaurant, RestaurantResponseDto.class);
    }

    @Override
    public void deleteById(Long id) {
        if(findById(id).isEmpty()){
            throw new RestaurantNotFoundException(
                    String.format("A restaurant with the Id '%d' doesn't exist", id));
        }
        this.restaurantRepository.deleteById(id);
    }
}
