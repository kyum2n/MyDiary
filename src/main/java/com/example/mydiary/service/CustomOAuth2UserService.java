package com.example.mydiary.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.mydiary.entity.Member;
import com.example.mydiary.oauth.OAuthAttributes;
import com.example.mydiary.repository.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberMapper memberMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "naver", "kakao"
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes: 사용자 정보 변환 클래스 (직접 구현한 것)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());

        // DB에 저장하거나 기존 유저 조회
        Member member = saveOrUpdate(attributes);

        // 세션에 loginUser 설정
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", member); // 세션 기반 접근도 병행 가능

        // SecurityContext에 인증 정보 수동 등록
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                member,
                null,
                Collections.singleton(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        Map<String, Object> customAttributes = new HashMap<>(attributes.getAttributes());
        customAttributes.put("provider", attributes.getProvider());

        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_USER"),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 사용자 저장 or 업데이트
    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member existing = memberMapper.findByuId(attributes.getUId());
        
        if (existing == null) {
            Member newUser = Member.builder()
                    .uId(attributes.getUId())
                    .uName(attributes.getUName())
                    .uPwd("social")
                    .uEmail(attributes.getUEmail())
                    .uImage(attributes.getUImage())
                    .provider(attributes.getProvider())
                    .providerId(attributes.getUId())
                    .build();
            memberMapper.insertMember(newUser);
            return newUser;
        } else {
            return existing;
        }
    }
}