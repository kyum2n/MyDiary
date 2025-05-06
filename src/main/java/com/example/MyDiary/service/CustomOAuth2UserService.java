package com.example.mydiary.service;

import com.example.mydiary.entity.Member;
import com.example.mydiary.oauth.OAuthAttributes;
import com.example.mydiary.repository.MemberMapper;

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

    private final MemberMapper memberMapper;  // ✅ MyBatis 매퍼로 변경
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        Map<String, Object> userAttributes = attributes.getAttributes();

        if (userAttributes.get("id") == null) {
            throw new OAuth2AuthenticationException("OAuth2 attribute 'id' is missing.");
        }

        String generatedUid = registrationId + "_" + attributes.getProviderId();

        // ✅ MyBatis 방식으로 사용자 조회
        Member member = memberMapper.findByUid(generatedUid);

        if (member == null) {
            // ✅ 없으면 삽입
            member = Member.builder()
                    .uId(generatedUid)
                    .uEmail(attributes.getEmail())
                    .uName(attributes.getName())
                    .uImage(attributes.getProfileImage())
                    .uPwd(passwordEncoder.encode("SOCIAL_USER")) // 더미 패스워드
                    .provider(registrationId)
                    .providerId(attributes.getProviderId())
                    .build();

            memberMapper.insertMember(member);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                "id"
        );
    }
}