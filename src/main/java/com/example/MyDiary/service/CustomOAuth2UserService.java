package com.example.mydiary.service;

import com.example.mydiary.entity.Member;
import com.example.mydiary.oauth.OAuthAttributes;
import com.example.mydiary.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "naver", "google"
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());

        Member member = memberRepository.findByuEmail(attributes.getEmail())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .uEmail(attributes.getEmail())
                        .uName(attributes.getName())
                        .uImage(attributes.getProfileImage())
                        .uPwd(passwordEncoder.encode("SOCIAL_USER"))
                        .provider(registrationId)
                        .providerId(attributes.getProviderId())
                        .build()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
}