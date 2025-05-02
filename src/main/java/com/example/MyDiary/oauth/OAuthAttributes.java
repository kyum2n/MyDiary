package com.example.mydiary.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final String email;
    private final String name;
    private final String profileImage;
    private final String providerId;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    @Builder
    public OAuthAttributes(String email, String name, String profileImage,
                           String providerId, Map<String, Object> attributes, String nameAttributeKey) {
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.providerId = providerId;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver(attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        } else { // google or default
            return ofGoogle(attributes);
        }
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .profileImage((String) attributes.get("picture"))
                .providerId((String) attributes.get("sub")) // 고유 ID
                .attributes(attributes)
                .nameAttributeKey("email")
                .build();
    }

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .profileImage((String) response.get("profile_image"))
                .providerId((String) response.get("id"))
                .attributes(response)
                .nameAttributeKey("email")
                .build();
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) profile.get("nickname"))
                .profileImage((String) profile.get("profile_image_url"))
                .providerId(String.valueOf(attributes.get("id")))
                .attributes(attributes)
                .nameAttributeKey("email")
                .build();
    }
}