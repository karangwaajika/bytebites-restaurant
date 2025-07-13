package com.lab.auth_service.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.auth_service.model.UserEntity;
import com.lab.auth_service.repository.UserRepository;
import com.lab.auth_service.security.jwt.JwtUtil;
import com.lab.auth_service.util.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil,
                                              UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication)
            throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request,
                response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest  req,
                                        HttpServletResponse res,
                                        Authentication      auth) throws IOException {

        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        String email = principal.getAttribute("email");
        System.out.println("my email"+ email);
        if (email == null) {
            // handle missing email case gracefully
            res.sendRedirect("/login?error=email_not_found");
            return;
        }

        // role from local DB
        if(userRepository.findByEmail(email).isEmpty()){
            System.out.println("not saved");
        }

        Role role = userRepository.findByEmail(email).get().getRole();
        // create a userDetails for token creation
        UserDetails userDetails = new User(
                email,                   // username → put your e‑mail here
                "",                      // password not needed for token creation
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
        UserEntity userEntity = userRepository.findByEmail(email).get();
        String token = jwtUtil.generateToken(userEntity);

        // Redirect with token in URL
         //res.sendRedirect("/#/oauth-success?token=" + token);

        //Return JSON
//        res.setContentType("application/json");
//        res.getWriter().write(
//                mapper.writeValueAsString(Map.of("token", token)));

        String encoded = URLEncoder.encode(token, StandardCharsets.UTF_8);
        res.sendRedirect("/oauth2/success?token=" + encoded);
    }

}
