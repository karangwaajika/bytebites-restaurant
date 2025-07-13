package com.lab.auth_service.controller;

import com.lab.auth_service.dto.UserResponseDto;
import com.lab.auth_service.exception.UserNotFoundException;
import com.lab.auth_service.model.UserEntity;
import com.lab.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
//@RequestMapping("api-auth-service")
@Tag(name = "User Controller", description = "Manage all the User's urls")
public class UserController {
    UserService userService;
    ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(name = "view_users", path = "/users/view")
    @Operation(summary = "View users",
            description = "This method applies pagination for efficient retrieval " +
                          "of users list")
    public Page<UserResponseDto> viewUsers(Pageable pageable){
        return this.userService.findAll(pageable);
    }

    @GetMapping(name = "view_user", path = "/users/me")
    @Operation(summary = "View the current user",
            description = "This method retrieve the authenticated user who is currently " +
                          "in interaction with the site")
    public ResponseEntity<UserResponseDto> viewCurrentUser(HttpServletRequest request){
        String userEmail = request.getHeader("X-User-Email");
        UserEntity user = this.userService.findByEmail(userEmail).get();
        return ResponseEntity.ok(modelMapper.map(user, UserResponseDto.class));
    }

//    for internal communication between services
    @GetMapping(name = "find_user_by_id", path = "users/view/{id}")
    @Operation(summary = "Find User",
            description = "Search and view only one user using user ID")
    public UserResponseDto viewTask(@PathVariable Long id){
        Optional<UserEntity> task = this.userService.findById(id);

        return task.map(userEntity -> this.modelMapper.map(userEntity, UserResponseDto.class)).orElse(null);
    }

}
