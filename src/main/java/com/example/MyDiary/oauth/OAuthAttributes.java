package com.example.mydiary.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes; // 소셜에서 받은 전체 응답 값
    private final String nameAttributeKey; // 사용자 식별 키 (ex. "id")
    
    // 사용자 기본 정보
    private final String name;
    private final String email;
    private final String profileImage;
    private final String providerId;

    // 사용자 정보 저장용 객체 생성
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

    // 소셜 플랫폼 구분 (현재는 네이버만)
    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
    }

    // 네이버 응답 처리
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null || response.get("id") == null) {
            throw new IllegalArgumentException("Missing 'id'");
        }

        // id가 반드시 포함되도록 복사
        Map<String, Object> extended = new HashMap<>(response);
        extended.put("id", response.get("id"));

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .profileImage((String) response.get("profile_image"))
                .providerId((String) response.get("id"))
                .attributes(extended)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 세션 저장용
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}