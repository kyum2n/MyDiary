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

    // 소셜 로그인 사용자 정보 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 사용자 정보 로딩 (OAuth 응답 전체)
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 소셜 플랫폼명(naver 등) 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 표준화된 사용자 정보로 변환
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        Map<String, Object> userAttributes = attributes.getAttributes();

        // id 없으면 예외 (세션 저장에 필수)
        if (userAttributes.get("id") == null) {
            throw new OAuth2AuthenticationException("OAuth2 attribute 'id' is missing.");
        }

        // UID 생성 (예: naver_아이디)
        String generatedUid = registrationId + "_" + attributes.getProviderId();

        // 회원이 없으면 새로 저장
        Member member = memberRepository.findByuId(generatedUid)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .uId(generatedUid)
                        .uEmail(attributes.getEmail())
                        .uName(attributes.getName())
                        .uImage(attributes.getProfileImage())
                        .uPwd(passwordEncoder.encode("SOCIAL_USER")) // 더미 비밀번호
                        .provider(registrationId)
                        .providerId(attributes.getProviderId())
                        .build()));

        // Spring Security 세션 저장용 사용자 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                "id" // 세션에서 사용자 ID로 사용할 키
        );
    }
}