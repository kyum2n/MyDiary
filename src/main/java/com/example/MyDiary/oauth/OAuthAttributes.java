package com.example.mydiary.oauth;

import java.util.Map;

public class OAuthAttributes {
    private final String name;
    private final String email;
    private final Map<String, Object> attributes;

    public OAuthAttributes(String name, String email, Map<String, Object> attributes) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return new OAuthAttributes((String) response.get("name"), (String) response.get("email"), response);
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return new OAuthAttributes((String) profile.get("nickname"), (String) kakaoAccount.get("email"), attributes);
        }
        // Google
        return new OAuthAttributes((String) attributes.get("name"), (String) attributes.get("email"), attributes);
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public Map<String, Object> getAttributes() { return attributes; }
}
