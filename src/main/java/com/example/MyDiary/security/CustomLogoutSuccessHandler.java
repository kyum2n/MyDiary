package com.example.mydiary.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 로그아웃 성공 시 동작을 직접 정의하는 핸들러
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        // 로그인 안 된 상태에서 로그아웃 요청한 경우
        if (authentication == null) {
            response.sendRedirect("/intro");
            return;
        }
        // 소셜 로그인 사용자면 /logout-success 페이지로 리다이렉트
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2User) {
            // 소셜 로그인 로그아웃 -> 먼저 /logout-success, 이후에는 그 페이지에서 meta 태그로 /intro 이동 처리
            response.sendRedirect("/logout-success");

            // 일반 로그인 사용자는 바로 /intro 이동
        } else {

            // 일반 로그인 로그아웃 -> 바로 intro
            response.sendRedirect("/intro");
        }
    }
}