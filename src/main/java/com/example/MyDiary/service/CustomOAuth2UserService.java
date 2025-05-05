package com.example.mydiary.service;

import com.example.mydiary.entity.Member;
import com.example.mydiary.oauth.OAuthAttributes;
import com.example.mydiary.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("=== [DEBUG] OAuth2 Original Raw Attributes ===");
        System.out.println(oAuth2User.getAttributes());
        System.out.println("=== [DEBUG] Transformed Attributes ===");
        System.out.println(OAuthAttributes.of(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()).getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());

        Map<String, Object> userAttributes = attributes.getAttributes();

        // ✅ "id" 키 존재 여부 검증
        Object id = userAttributes.get("id");
        if (id == null) {
            throw new OAuth2AuthenticationException("OAuth2 attribute 'id' is missing. Full response: " + userAttributes);
        }

        String generatedUid = registrationId + "_" + attributes.getProviderId();

        Member member = memberRepository.findByuId(generatedUid)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .uId(generatedUid)
                        .uEmail(attributes.getEmail())
                        .uName(attributes.getName())
                        .uImage(attributes.getProfileImage())
                        .uPwd(passwordEncoder.encode("SOCIAL_USER"))
                        .provider(registrationId)
                        .providerId(attributes.getProviderId())
                        .build()));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                "id"
        );
    }
}