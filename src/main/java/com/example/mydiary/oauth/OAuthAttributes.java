package com.example.mydiary.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String name;
    private final String email;
    private final String profileImage;
    private final String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String name, String email, String profileImage,
            String providerId) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.providerId = providerId;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        System.out.println("🔴 OAuthAttributes.of invoked with: " + registrationId);
        if ("naver".equals(registrationId)) {
            return ofNaver(attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        }
        return ofGoogle(attributes);
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImage((String) attributes.get("picture"))
                .providerId((String) attributes.get("sub"))
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .profileImage((String) response.get("profile_image"))
                .providerId((String) response.get("id"))
                .attributes(response)
                .build();
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;

        String nickname = profile != null ? (String) profile.get("nickname") : "카카오유저";
        String email = kakaoAccount != null && kakaoAccount.get("email") != null
                ? (String) kakaoAccount.get("email")
                : "kakao_" + attributes.get("id") + "@kakao.com";
        String profileImage = profile != null ? (String) profile.get("profile_image_url") : "default.png";

        System.out.println("🔥 카카오 nickname: " + nickname);
        System.out.println("🔥 카카오 email: " + email);
        System.out.println("🔥 카카오 providerId: " + attributes.get("id"));

        return OAuthAttributes.builder()
                .name(nickname)
                .email(email)
                .profileImage(profileImage)
                .providerId(String.valueOf(attributes.get("id")))
                .attributes(attributes)
                .build();
    }
}