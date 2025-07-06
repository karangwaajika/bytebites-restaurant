package com.lab.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("prod")
public class HttpsEnforceConfig {
    @Bean
    @Order(99)                    // to keep it last in the chain list
    public SecurityFilterChain httpsEnforcedChain(HttpSecurity http) throws Exception {
        http.requiresChannel(c -> c.anyRequest().requiresSecure());
        return http.build();
    }
}
