package com.lab.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {
    private Key key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        System.out.println("Goood here");
        System.out.println("Secret length: " + secretKey.length());
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        System.out.println("JWT secret loaded, key generated");
    }

    public Claims validateTokenAndGetClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
