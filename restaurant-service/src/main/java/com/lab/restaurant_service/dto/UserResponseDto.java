package com.lab.restaurant_service.dto;

import com.lab.restaurant_service.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private Role role;
}
