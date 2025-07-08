package com.lab.restaurant_service.controller;

import com.lab.restaurant_service.dto.RestaurantRequestDto;
import com.lab.restaurant_service.dto.RestaurantResponseDto;
import com.lab.restaurant_service.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {
    ModelMapper modelMapper;
    RestaurantService restaurantService;

    public RestaurantController(ModelMapper modelMapper,
                                RestaurantService restaurantService) {
        this.modelMapper = modelMapper;
        this.restaurantService = restaurantService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add restaurant",
            description = "This request inserts a restaurant to the database and returns " +
                          "the inserted restaurant ")
    public ResponseEntity<RestaurantResponseDto> registerUser(
            @RequestBody RestaurantRequestDto restaurantRequestDto
    ) {
        RestaurantResponseDto savedUser = this.restaurantService.create(restaurantRequestDto);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping(name = "view_restaurants", path = "/view")
    @Operation(summary = "View restaurants",
            description = "This method applies pagination for efficient retrieval " +
                          "of restaurants list")
    public Page<RestaurantResponseDto> viewUsers(Pageable pageable){
        return this.restaurantService.findAll(pageable);
    }

}
