package com.lab.auth_service.controller;

import com.lab.auth_service.dto.UserResponseDto;
import com.lab.auth_service.model.UserEntity;
import com.lab.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Tag(name = "User Controller", description = "Manage all the User's urls")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(name = "view_users", path = "/users/view")
    @Operation(summary = "View users",
            description = "This method applies pagination for efficient retrieval " +
                          "of users list")
    public Page<UserResponseDto> viewUsers(Pageable pageable){
        return this.userService.findAll(pageable);
    }

    @GetMapping(name = "view_users", path = "/admin/users")
    @Operation(summary = "View users",
            description = "This method applies pagination for efficient retrieval " +
                          "of users list")
    public Page<UserResponseDto> adminOnlyViewUsers(Pageable pageable){
        return this.userService.findAll(pageable);
    }


    @GetMapping(name = "view_user", path = "/users/me")
    @Operation(summary = "View the current user",
            description = "This method retrieve the authenticated user who is currently " +
                          "in interaction with the site")
    public UserEntity viewCurrentUser(Authentication auth){
        return this.userService.findByEmail(auth.getName()).get();
    }

}
