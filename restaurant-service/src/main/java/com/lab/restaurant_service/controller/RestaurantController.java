package com.lab.restaurant_service.controller;

import com.lab.restaurant_service.dto.RestaurantRequestDto;
import com.lab.restaurant_service.dto.RestaurantResponseDto;
import com.lab.restaurant_service.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestController
@RequestMapping("restaurants")
public class RestaurantController {
    ModelMapper modelMapper;
    RestaurantService restaurantService;

    public RestaurantController(ModelMapper modelMapper,
                                RestaurantService restaurantService) {
        this.modelMapper = modelMapper;
        this.restaurantService = restaurantService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PostMapping("/add")
    @Operation(summary = "Add restaurant",
            description = "This request inserts a restaurant to the database and returns " +
                          "the inserted restaurant ")
    public ResponseEntity<RestaurantResponseDto> addRestaurant(
            @RequestBody RestaurantRequestDto restaurantRequestDto,
            HttpServletRequest request
    ) throws Exception {
        RestaurantResponseDto savedUser = this.restaurantService.create(restaurantRequestDto);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping(name = "view_restaurants", path = "/view")
    @Operation(summary = "View restaurants",
            description = "This method applies pagination for efficient retrieval " +
                          "of restaurants list")
    public Page<RestaurantResponseDto> viewUsers(Pageable pageable, HttpServletRequest request){
        String userId = request.getHeader("X-User-ID");
        String userEmail = request.getHeader("X-User-Email");
        System.out.println(userId+" : "+userEmail);
        return this.restaurantService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @DeleteMapping(name = "delete_restaurant", path = "/delete")
    @Operation(summary = "Delete Restaurant",
            description = "The restaurant is delete using its id that is retrieved " +
                          "as a query parameter from the url")
    public ResponseEntity<?> deleteRestaurant(@RequestParam Long id){
        this.restaurantService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Restaurant deleted successfully"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PatchMapping(name = "update_restaurant", path = "/update")
    @Operation(summary = "Update Restaurant",
            description = "The restaurant can be updated partially, " +
                          "it's doesn't necessary required " +
                          "all the fields to be updated")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@RequestBody RestaurantRequestDto restaurantDto,
                                                      @RequestParam Long restaurantId){

        RestaurantResponseDto updatedRestaurant = this.restaurantService.partialUpdate(restaurantDto, restaurantId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedRestaurant);
    }
}
