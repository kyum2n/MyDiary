package com.example.mydiary.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class CustomLogoutController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @RequestMapping(value = "/customLogout", method = { RequestMethod.GET, RequestMethod.POST })
    public String handleLogout(HttpSession session) {
        String provider = (session != null) ? (String) session.getAttribute("loginProvider") : null;

        // 세션 무효화
        if (session != null) {
            session.invalidate();
        }

        // 플랫폼별 로그아웃 처리
        if ("kakao".equals(provider)) {
            String kakaoRedirectUri = "http://localhost:8080/logout-kakao";
            return "redirect:https://kauth.kakao.com/oauth/logout?client_id=" + kakaoClientId +
                    "&logout_redirect_uri=" + kakaoRedirectUri;
        } else if ("naver".equals(provider)) {
            return "redirect:https://nid.naver.com/nidlogin.logout?returl=http://localhost:8080/intro";
        } else {
            return "redirect:/intro"; // 일반 로그인
        }
    }
}