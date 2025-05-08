package com.example.mydiary.service;

import com.example.mydiary.entity.Member;
import com.example.mydiary.oauth.OAuthAttributes;
import com.example.mydiary.repository.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;
    private final HttpServletRequest request; // 세션 접근

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("loadUser() 진입 완료: " + registrationId);

        OAuth2User oAuth2User;
        try {
            oAuth2User = super.loadUser(userRequest);
        } catch (Exception e) {
            System.out.println("super.loadUser() 실패: " + e.getMessage());
            throw e;
        }

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        System.out.println("registrationId = " + registrationId);
        System.out.println("userNameAttributeName = " + userNameAttributeName);
        System.out.println("원시 attributes: " + oAuth2User.getAttributes());

        OAuthAttributes attributes;
        try {
            attributes = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        } catch (Exception e) {
            System.out.println("OAuthAttributes.of() 실패: " + e.getMessage());
            throw e;
        }

        Member member;
        try {
            member = saveOrUpdate(attributes);
        } catch (Exception e) {
            System.out.println("saveOrUpdate() 실패: " + e.getMessage());
            throw e;
        }

        Map<String, Object> userAttributes = new HashMap<>(attributes.getAttributes());

        if (!userAttributes.containsKey(userNameAttributeName) || userAttributes.get(userNameAttributeName) == null) {
            System.out.println("😰 " + userNameAttributeName + " 없음. providerId로 대체");
            userAttributes.put(userNameAttributeName, attributes.getProviderId());
        }

        userAttributes.put("provider", registrationId);
        userAttributes.putIfAbsent("id", attributes.getProviderId());

        // 세션에 provider 저장 (logout-success.html에서 분기용)
        request.getSession().setAttribute("provider", registrationId);

        try {
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    userAttributes,
                    userNameAttributeName);
        } catch (Exception e) {
            System.out.println("DefaultOAuth2User 생성 실패: " + e.getMessage());
            throw e;
        }
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member existingMember = memberMapper.findByEmail(attributes.getEmail());

        if (existingMember == null) {
            Member newMember = new Member();
            newMember.setUId("social_" + attributes.getProviderId());
            newMember.setUName(attributes.getName());
            newMember.setUPwd("social");
            newMember.setUEmail(attributes.getEmail());
            newMember.setUImage(attributes.getProfileImage());
            newMember.setProvider("social");
            newMember.setProviderId(attributes.getProviderId());

            System.out.println("새 사용자 등록: " + newMember.getUEmail());
            memberMapper.insertMember(newMember);
            return newMember;
        }

        System.out.println("기존 사용자 로그인: " + existingMember.getUEmail());
        return existingMember;
    }
}