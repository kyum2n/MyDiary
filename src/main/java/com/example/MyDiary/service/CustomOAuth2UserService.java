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

        // ▶ 소셜 플랫폼 이름 (예: naver, google 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // ▶ 소셜 응답에서 사용자 정보 추출 (이메일, 이름 등)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());

        // ✅ uId 값을 명시적으로 생성: 예) naver_af123xyz
        String generatedUid = registrationId + "_" + attributes.getProviderId();

        // ✅ uId 기준으로 사용자 조회, 없으면 새로 생성
        Member member = memberRepository.findByuId(generatedUid)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .uId(generatedUid)
                        .uEmail(attributes.getEmail())
                        .uName(attributes.getName())
                        .uImage(attributes.getProfileImage())
                        .uPwd(passwordEncoder.encode("SOCIAL_USER")) // 실제 사용 X, 형식용
                        .provider(registrationId)
                        .providerId(attributes.getProviderId())
                        .build()));

        // ✅ OAuth2User 리턴 (세션 저장용)
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
}