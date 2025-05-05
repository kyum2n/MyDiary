package com.example.mydiary.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String profileImage;
    private final String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String name, String email, String profileImage, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.providerId = providerId;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null || response.get("id") == null) {
            throw new IllegalArgumentException("Naver OAuth response is missing required 'id'");
        }

        // ✅ id를 포함한 response 복사본 생성
        Map<String, Object> extended = new HashMap<>(response);
        extended.put("id", response.get("id")); // DefaultOAuth2User에서 사용할 수 있도록 id 명시

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .profileImage((String) response.get("profile_image"))
                .providerId((String) response.get("id"))
                .attributes(extended)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}