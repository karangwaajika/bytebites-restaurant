package com.lab.auth_service.service;

import com.lab.auth_service.model.UserEntity;
import com.lab.auth_service.repository.UserRepository;
import com.lab.auth_service.util.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder; // still needed for “local” passwords

    public OAuth2UserService(UserRepository userRepository,
                             PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder  = encoder;
        System.out.println("I;m service");
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = super.loadUser(req);
        System.out.println("I;m service 2");

        String email = extractEmail(oauthUser, req.getClientRegistration().getRegistrationId());
        System.out.println("in loader "+email);
        // ①  If the user doesn’t exist, create one with ROLE_CONTRACTOR
        userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity u = new UserEntity();
            u.setEmail(email);
            u.setPassword(               // not used for OAuth login, but cannot be null
                    encoder.encode(UUID.randomUUID().toString()));
            u.setRole(Role.CUSTOMER);
            return userRepository.save(u);
        });


        // Map Spring‑Security authority from local DB role
        Role role = userRepository.findByEmail(email).get().getRole();

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        Map<String, Object> attrs = new HashMap<>(oauthUser.getAttributes());
        attrs.put("email", email);

        return new DefaultOAuth2User(authorities,
                attrs,
                "id"); // key of the unique attribute
    }

    private String extractEmail(OAuth2User user, String registrationId) {
        System.out.println("I;m service 3");
        if ("google".equals(registrationId)) {
            System.out.println("here in google");
            return (String) user.getAttributes().get("email");
        }
        if ("github".equals(registrationId)) {
            System.out.println("here in git");
            Object emailAttr = user.getAttributes().get("email");
            if (emailAttr != null) return emailAttr.toString();
            // fallback to login if email is not present
            Object login = user.getAttributes().get("login");
            if (login != null) return login.toString() + "@github"; // construct dummy email
        }
        System.out.println("nothing end");
        throw new OAuth2AuthenticationException("Could not determine email from " + registrationId);

    }
}
