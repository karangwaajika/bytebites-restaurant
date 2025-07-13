package com.lab.api_gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter{
    private final JwtUtil jwtUtil;
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/login",
            "/register"
    );

    // check if the path is public
    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.contains(path);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("P "+path);

        // Skip auth for public endpoints
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("authHeader :"+authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                System.out.println("here we are");
                String token = authHeader.substring(7);
                System.out.println("token: "+token);
                Claims claims = jwtUtil.validateTokenAndGetClaims(token);
                Object userIdObj = claims.get("userId");
                String userId = userIdObj != null ? userIdObj.toString() : null;
                String email = claims.get("email", String.class);
                String role = claims.get("role", String.class);

                System.out.println("role: "+role);



                // Build new request with additional headers
                ServerHttpRequest mutatedRequest = exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Email", email)
                        .header("X-User-Role", role)
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                return chain.filter(mutatedExchange);
            } catch (Exception e) {
                // Invalid token - reject request
                e.printStackTrace();
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        // No Authorization header - reject request
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
