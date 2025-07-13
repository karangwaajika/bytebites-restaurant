package com.lab.order_service.service;

import com.lab.order_service.dto.MenuDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MenuService {
    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public MenuService(RestTemplate restTemplate,
                       @Value("${auth.service.url:http://localhost:8080/api-restaurant-service/menu}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public MenuDto findMenuById(Long menuId) throws Exception {
        try {
            String url = authServiceUrl + "/view/" + menuId;
            ResponseEntity<MenuDto> response = restTemplate.getForEntity(url, MenuDto.class);
            return response.getBody();
        } catch (Exception e) {
            throw new Exception("Failed to fetch menu with id: " + menuId, e);
        }
    }

    public boolean isMenuExists(Long userId) {
        try {
            MenuDto menuDto = findMenuById(userId);
            return menuDto != null;
        } catch (Exception e) {
            return false;
        }
    }
}
