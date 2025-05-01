package com.example.mydiary.service;

import com.example.mydiary.entity.User;
import com.example.mydiary.oauth.OAuthAttributes;
import com.example.mydiary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());

        User user = userRepository.findByUsername(attributes.getEmail())
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(attributes.getEmail())
                        .displayName(attributes.getName())
                        .password("SOCIAL_LOGIN")
                        .role("ROLE_USER")
                        .build()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                attributes.getAttributes(),
                "email"
        );
    }
}