package com.example.mydiary.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final String uId;
    private final String uName;
    private final String uEmail;
    private final String uImage;
    private final String provider;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    @Builder
    public OAuthAttributes(String uId, String uName, String uEmail, String uImage,
            String provider, Map<String, Object> attributes, String nameAttributeKey) {
        this.uId = uId;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uImage = uImage;
        this.provider = provider;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
            Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("Unsupported provider: " + registrationId);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .uId((String) response.get("id"))
                .uName((String) response.get("name"))
                .uEmail((String) response.get("email"))
                .uImage((String) response.get("profile_image"))
                .provider("naver")
                .attributes(response)
                .nameAttributeKey("id")
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;

        String nickname = profile != null ? (String) profile.get("nickname") : "카카오유저";
        String email = kakaoAccount != null && kakaoAccount.get("email") != null
                ? (String) kakaoAccount.get("email")
                : "kakao_" + attributes.get("id") + "@kakao.com";
        String profileImage = profile != null ? (String) profile.get("profile_image_url")
                : "/image/defaultProfileImage.webp";

        return OAuthAttributes.builder()
                .uId(String.valueOf(attributes.get("id")))
                .uName(nickname)
                .uEmail(email)
                .uImage(profileImage)
                .provider("kakao")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}