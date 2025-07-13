package com.lab.auth_service.security;

import com.lab.auth_service.repository.UserRepository;
import com.lab.auth_service.security.jwt.JwtAuthenticationFilter;
import com.lab.auth_service.security.jwt.JwtAuthorizationFilter;
import com.lab.auth_service.security.jwt.JwtUtil;
import com.lab.auth_service.security.oauth.OAuth2AuthenticationSuccessHandler;
import com.lab.auth_service.service.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class ApplicationSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    private final OAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2LoginSuccessHandler;

    public ApplicationSecurityConfig(JwtUtil jwtUtil,
                                     UserDetailsService userDetailsService,
                                     UserRepository userRepository,
                                     OAuth2UserService customOAuth2UserService,
                                     OAuth2AuthenticationSuccessHandler oAuth2LoginSuccessHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/users/view/*",
                                "restaurants/**",
                                "tasks/**",
                                "/logs/view",
                                "/users/view",
                                "/restaurants/**",
                                "/menu/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/delete/*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((req, res, ae) -> {
                            res.setStatus(HttpStatus.FORBIDDEN.value());
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Forbidden\"}");
                        })
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)), jwtUtil, userDetailsService));
        return httpSecurity.build();
    }

    //  for restaurant-service
    @Bean
    @Order(2)
    public SecurityFilterChain restaurantSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api-restaurant-service/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-restaurant-service/view").authenticated()
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }

    //  for order-service
    @Bean
    @Order(3)
    public SecurityFilterChain orderSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api-order-service/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-order-service/**").permitAll()
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }

    //  for order-service
    @Bean
    @Order(4)
    public SecurityFilterChain menuSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api-menu-service/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-menu-service/**").permitAll()
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }

    /*@Bean
    @Order(5)
    public SecurityFilterChain webChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html",
                                "/swagger-ui/**","/").permitAll()
                        .requestMatchers(
                                "/css/**", "/js/**","/webjars/**", "/images/**",
                                "/login", "/oauth2/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login"))       // optional classic login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true));

        return http.build();
    } */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
