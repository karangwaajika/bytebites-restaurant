package com.lab.auth_service.dto;


import com.lab.auth_service.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    private String email;
    private String password;
    private Role role;
}
