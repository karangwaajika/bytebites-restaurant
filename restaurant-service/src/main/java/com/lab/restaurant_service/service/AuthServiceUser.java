package com.lab.restaurant_service.service;

import com.lab.restaurant_service.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceUser {
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthServiceUser(RestTemplate restTemplate,
                           @Value("${auth.service.url:http://localhost:8080/api-auth-service}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public UserResponseDto findUserById(Long userId) throws Exception {
        try {
            String url = authServiceUrl + "/users/view/" + userId;
            ResponseEntity<UserResponseDto> response = restTemplate.getForEntity(url, UserResponseDto.class);
            return response.getBody();
        } catch (Exception e) {
            throw new Exception("Failed to fetch user with id: " + userId, e);
        }
    }

    public boolean isUserExists(Long userId) {
        try {
            UserResponseDto user = findUserById(userId);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }
}
