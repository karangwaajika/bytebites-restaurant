package com.lab.auth_service.controller;

import com.lab.auth_service.dto.AuthenticationRequest;
import com.lab.auth_service.dto.AuthenticationResponse;
import com.lab.auth_service.dto.UserRegisterDto;
import com.lab.auth_service.dto.UserResponseDto;
import com.lab.auth_service.model.UserEntity;
import com.lab.auth_service.security.jwt.JwtUtil;
import com.lab.auth_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication Controller",
        description = "Manage all the Authentication 's urls")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthenticationController(UserService userService,
                                    JwtUtil jwtUtil,
                                    AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService) {

        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user",
            description = "This request inserts a user to the database and returns " +
                                                        "the inserted user ")
    public ResponseEntity<UserResponseDto> registerUser(
            @RequestBody UserRegisterDto userRegisterDto
            ) {
        UserResponseDto savedUser = this.userService.create(userRegisterDto);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Log user in",
            description = "Upon successful credentials, user is authenticated " +
                          "and a new token is generated")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) {

        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        final UserEntity userDetails = this.userService.findByEmail(authenticationRequest.getEmail()).get();
        final String jwt = this.jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}

