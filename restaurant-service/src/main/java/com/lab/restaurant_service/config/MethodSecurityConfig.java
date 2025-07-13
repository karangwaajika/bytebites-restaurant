package com.lab.restaurant_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity // enables @PreAuthorize, @PostAuthorize, etc.
public class MethodSecurityConfig {
    // no code needed here, just the annotation
}
